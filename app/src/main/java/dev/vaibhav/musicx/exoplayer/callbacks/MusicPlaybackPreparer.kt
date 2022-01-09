package dev.vaibhav.musicx.exoplayer.callbacks

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import dev.vaibhav.musicx.exoplayer.datasource.MusicPlayerDataSource

class MusicPlaybackPreparer(
    private val dataSource: MusicPlayerDataSource,
    private val onPlayerPrepared: (MediaMetadataCompat) -> Unit
) : MediaSessionConnector.PlaybackPreparer {
    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false

    override fun getSupportedPrepareActions(): Long {
        return PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
    }

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle?) {
        dataSource.whenReady {
            val songToBePlayed =
                dataSource.allMusicAsMetadata.find { it.description.mediaId == mediaId }
            songToBePlayed?.let { onPlayerPrepared(it) }
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit
}
