package dev.vaibhav.musicx.exoplayer

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.vaibhav.musicx.utils.ErrorType
import dev.vaibhav.musicx.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicServiceConnection @Inject constructor(@ApplicationContext private val context: Context) {

    private val _connectionEvent = MutableSharedFlow<Resource<Boolean>>()
    val connectionEvent = _connectionEvent.asSharedFlow()

    private val _currentSong = MutableStateFlow<MediaMetadataCompat?>(null)
    val currentSong = _currentSong.asStateFlow()

    private val _playbackState = MutableStateFlow<PlaybackStateCompat?>(null)
    val playbackState = _playbackState.asStateFlow()

    private val connectionCallback = ConnectionCallback()

    private lateinit var mediaController: MediaControllerCompat

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val mediaBrowser = MediaBrowserCompat(
        context, ComponentName(context, MusicService::class.java), connectionCallback, null
    ).apply { connect() }

    private val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    fun subscribe(parentId: String, callbacks: MediaBrowserCompat.SubscriptionCallback) {
        if (!mediaBrowser.isConnected)
            mediaBrowser.subscribe(parentId, callbacks)
    }

    fun unsubscribe(parentId: String) {
        mediaBrowser.unsubscribe(parentId)
    }

    fun reconnect() {
        mediaBrowser.disconnect()
        mediaBrowser.connect()
    }

    fun stopPlaying() = transportControls.stop()

    fun seekTo(pos: Long) = transportControls.seekTo(pos)

    fun pause() = transportControls.pause()

    fun play() = transportControls.play()

    fun fastForward() = transportControls.fastForward()

    fun rewind() = transportControls.rewind()

    fun skipToNextTrack() = transportControls.skipToNext()

    fun skipToPrev() = transportControls.skipToPrevious()

    fun playFromMediaId(mediaId: String) = transportControls.playFromMediaId(mediaId, null)

    private inner class ConnectionCallback : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            super.onConnected()
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }
            emit { _connectionEvent.emit(Resource.Success(true)) }
        }

        override fun onConnectionSuspended() {
            super.onConnectionSuspended()
            emit { _connectionEvent.emit(Resource.Error(message = "connection suspended")) }
        }

        override fun onConnectionFailed() {
            super.onConnectionFailed()
            emit { _connectionEvent.emit(Resource.Error(message = "Failed to connect")) }
        }
    }

    private inner class MediaControllerCallback() : MediaControllerCompat.Callback() {
        override fun onSessionDestroyed() {
            super.onSessionDestroyed()
            connectionCallback.onConnectionSuspended()
        }

        override fun onSessionEvent(event: String?, extras: Bundle?) {
            super.onSessionEvent(event, extras)
            if (event == ErrorType.NO_INTERNET.name)
                emit { _connectionEvent.emit(Resource.Error(message = "Failed to connect")) }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
            emit { _playbackState.emit(state) }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
            emit { _currentSong.emit(metadata) }
        }
    }

    private fun emit(emission: suspend () -> Unit) = coroutineScope.launch {
        emission()
    }
}
