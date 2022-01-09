package dev.vaibhav.musicx.ui.usecases

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import dev.vaibhav.musicx.exoplayer.*
import dev.vaibhav.musicx.utils.MEDIA_ROOT_ID
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MusicUseCase @Inject constructor(private val musicConnection: MusicServiceConnection) {

    val currentSong = musicConnection.currentSong
    val playbackState = musicConnection.playbackState

    val timePassed = flow {
        while (true) {
            val duration = playbackState.value?.currentPlaybackPosition
                ?: 0
//            if (uiState.value.musicSliderState.timePassed != duration)
            emit(duration)
            delay(1000L)
        }
    }

    suspend fun subscribeToService(): Resource<List<MediaBrowserCompat.MediaItem>> =
        suspendCoroutine {
            musicConnection.subscribe(
                MEDIA_ROOT_ID,
                object : MediaBrowserCompat.SubscriptionCallback() {
                    override fun onChildrenLoaded(
                        parentId: String,
                        children: MutableList<MediaBrowserCompat.MediaItem>
                    ) {
                        super.onChildrenLoaded(parentId, children)
                        Timber.d("children loaded $children")
                        it.resume(Resource.Success(children))
                    }

                    override fun onError(parentId: String) {
                        super.onError(parentId)
                        it.resume(Resource.Error(message = "Failed to subscribe"))
                    }
                }
            )
        }

    fun unsubscribeToService() {
        musicConnection.unsubscribe(MEDIA_ROOT_ID)
    }

    fun skipToNextTrack() = musicConnection.skipToNextTrack()

    fun skipToPrevTrack() = musicConnection.skipToPrev()

    fun seekTo(pos: Long) = musicConnection.seekTo(pos)

    fun fastForward() = musicConnection.fastForward()

    fun rewind() = musicConnection.rewind()

    fun playFromMediaId(mediaId: String) = musicConnection.playFromMediaId(mediaId)

    fun isMusicPlayingOrPaused() = musicConnection.playbackState.value?.let {
        return@let it.isPlaying || it.isPlayEnabled
    } ?: false

    fun playPause(musicId: String, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (isPrepared && musicId == currentSong.value?.getString(METADATA_KEY_MEDIA_ID))
            playPauseCurrentSong(toggle)
        else playFromMediaId(musicId)
    }

    private fun playPauseCurrentSong(toggle: Boolean) {
        playbackState.value?.let {
            when {
                it.isPlaying -> if (toggle) musicConnection.pause()
                it.isPlayEnabled -> musicConnection.play()
                else -> Unit
            }
        }
    }
}
