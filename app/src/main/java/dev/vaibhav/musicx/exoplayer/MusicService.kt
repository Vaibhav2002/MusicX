package dev.vaibhav.musicx.exoplayer

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.hilt.android.AndroidEntryPoint
import dev.vaibhav.musicx.exoplayer.callbacks.MediaPlayerListener
import dev.vaibhav.musicx.exoplayer.callbacks.MusicPlaybackPreparer
import dev.vaibhav.musicx.exoplayer.datasource.MusicPlayerDataSource
import dev.vaibhav.musicx.utils.MEDIA_ROOT_ID
import dev.vaibhav.musicx.utils.NETWORK_ERROR
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val MEDIA_SESSION = "MusicXMediaSession"

@AndroidEntryPoint
class MusicService : MediaBrowserServiceCompat() {

    companion object {
        var currentSongDuration = 0L
            private set
    }

    @Inject
    lateinit var exoPlayer: ExoPlayer

    @Inject
    lateinit var musicDataSource: MusicPlayerDataSource

    @Inject
    lateinit var dataSourceFactory: DefaultDataSource.Factory

    var isRunning = false
    private var isPlayerInitialized = false
    private val currentSong = MutableStateFlow<MediaMetadataCompat?>(null)

    private val serviceJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaPlayerListener: MediaPlayerListener
    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var connector: MediaSessionConnector
    private lateinit var musicNotificationManager: MusicNotificationManager
    private lateinit var playBackPreparer: MusicPlaybackPreparer

    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch {
            musicDataSource.getMusic()
        }
        initialize()
        connector.setPlayer(exoPlayer)
        connector.setPlaybackPreparer(playBackPreparer)
        connector.setQueueNavigator(QueueNavigator(mediaSession))
        exoPlayer.addListener(mediaPlayerListener)
        musicNotificationManager.showNotification(exoPlayer)
    }

    private fun initialize() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)?.let {
            PendingIntent.getActivity(this, 0, it, FLAG_IMMUTABLE)
        }
        mediaSession = MediaSessionCompat(this, MEDIA_SESSION).apply {
            setSessionActivity(intent)
            isActive = true
        }
        connector = MediaSessionConnector(mediaSession)
        sessionToken = mediaSession.sessionToken
        dataSourceFactory = DefaultDataSource.Factory(this)
        mediaPlayerListener = MediaPlayerListener(this)
        musicNotificationManager = MusicNotificationManager(this, mediaSession.sessionToken) {
            currentSongDuration = exoPlayer.duration
        }
        playBackPreparer = MusicPlaybackPreparer(musicDataSource) {
            coroutineScope.launch {
                currentSong.emit(it)
                prepareMusic(it, musicDataSource.allMusicAsMetadata, true)
            }
        }
    }

    private fun prepareMusic(
        songToBePlayed: MediaMetadataCompat,
        allSongs: List<MediaMetadataCompat>,
        playNow: Boolean
    ) {
        val songIndex =
            if (currentSong.value == null) 0
            else allSongs.indexOfFirst { it.description.mediaId == songToBePlayed.description.mediaId }
//        exoPlayer.setMediaSource(musicDataSource.asMediaSource(dataSourceFactory))
        exoPlayer.prepare(musicDataSource.asMediaSource(dataSourceFactory))
        if (songIndex != -1)
            exoPlayer.seekTo(songIndex, 0L)
        exoPlayer.playWhenReady = playNow
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot(MEDIA_ROOT_ID, null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        when (parentId) {
            MEDIA_ROOT_ID -> {
                val resultSend = musicDataSource.whenReady { isReady ->
                    if (isReady) {
                        result.sendResult(musicDataSource.allMusicAsMediaItem.toMutableList())
                        if (!isPlayerInitialized && musicDataSource.allMusic.isNotEmpty()) {
                            musicDataSource.allMusicAsMetadata.also {
                                prepareMusic(it[0], it, false)
                            }
                            isPlayerInitialized = true
                        }
                    } else {
                        mediaSession.sendSessionEvent(NETWORK_ERROR, null)
                        result.sendError(null)
                    }
                }
                if (!resultSend) result.detach()
            }
        }
    }

    private inner class QueueNavigator(mediaSessionCompat: MediaSessionCompat) :
        TimelineQueueNavigator(mediaSessionCompat) {
        override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat {
            return musicDataSource.allMusicAsMetadata[windowIndex].description
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exoPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(mediaPlayerListener)
        exoPlayer.release()
        coroutineScope.cancel()
    }
}
