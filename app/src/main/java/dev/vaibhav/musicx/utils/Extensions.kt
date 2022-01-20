package dev.vaibhav.musicx.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Images.ImageColumns.DATA
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION
import android.support.v4.media.session.PlaybackStateCompat
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.exoplayer.isPlaying
import dev.vaibhav.musicx.exoplayer.isPrepared
import timber.log.Timber

infix fun <T, F> Resource<T>.mapTo(change: (T) -> F): Resource<F> = when (this) {
    is Resource.Error -> Resource.Error(errorType, message)
    is Resource.Loading -> Resource.Loading()
    is Resource.Success -> Resource.Success(data?.let { change(it) }, message)
}

fun Resource<*>.mapToUnit() = this mapTo {}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.formatDuration(): String {
    var seconds = this / 1000
    val minutes = seconds / 60
    seconds %= 60
    return String.format("%02d:%02d", minutes, seconds)
}

fun List<String>.getArtistsString() = joinToString(", ")

fun MediaMetadataCompat.getMusic(): Music = Music(
    id = description.mediaId ?: "",
    title = description.title.toString(),
    duration = getLong(METADATA_KEY_DURATION),
    artists = description.subtitle.toString().split(","),
    imageUrl = description.iconUri.toString(),
    musicUrl = description.mediaUri ?: Uri.EMPTY
)

fun MediaBrowserCompat.MediaItem.getMusic(): Music = Music(
    id = description.mediaId ?: "",
    title = description.title.toString(),
    duration = description.extras?.getLong(DURATION) ?: 0,
    artists = description.subtitle.toString().split(","),
    imageUrl = description.iconUri.toString(),
    musicUrl = description.mediaUri ?: Uri.EMPTY
)

fun Context.loadImageBitmap(url: String, onImageLoaded: (Bitmap) -> Unit) {
    Glide.with(this).asBitmap().load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                onImageLoaded(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) = Unit
        })
}

fun PlaybackStateCompat.getMusicState(): MusicState = when {
    isPlaying -> MusicState.PLAYING
    isPrepared -> MusicState.PAUSED
    else -> MusicState.NONE
}

suspend fun Context.getLocalMusicList(dispatcher: Dispatcher): List<Music> {
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM_ID
    )
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    val cursor = contentResolver.query(
        collection,
        projection,
        null,
        null,
        sortOrder
    )
    val musicList = cursor?.getMusic(this) ?: emptyList()
    return musicList
}

private suspend fun Cursor.getMusic(context: Context): List<Music> {
    val musicList = mutableListOf<Music>()
    val idColumn = getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val albumIdColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
    val titleColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val durationColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
    val artistsColumn = getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    while (moveToNext()) {
        val id = getLong(idColumn)
        val title = getString(titleColumn)
        val duration = getLong(durationColumn)
        val albumId = getLong(albumIdColumn)
        val artists = getString(artistsColumn).split(" ")
        val musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
        Timber.d("File path = $musicUri")
        val imageUrl = getAlbumArt(albumId).toString()
        musicList += Music(id.toString(), title, duration, artists, imageUrl, musicUri)
    }
    return musicList
}

private fun Context.getFilePathFromUri(uri: Uri): String {
    Timber.d("URI $uri")
    return if ("content" == uri.scheme) {
        val cursor = contentResolver.query(uri, arrayOf(DATA), null, null, null)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(0) ?: ""
        cursor?.close()
        filePath
    } else {
        uri.path ?: ""
    }
}

private suspend fun getAlbumArt(album_id: Long) =
    ContentUris.withAppendedId(Uri.parse(sArtworkUri), album_id)
