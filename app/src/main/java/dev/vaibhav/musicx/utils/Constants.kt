package dev.vaibhav.musicx.utils

const val NOTIFICATION_ID = 1
const val CHANNEL_ID = "MusicXChannel"
const val CHANNEL_NAME = "MusicX"
const val MEDIA_ROOT_ID = "root_id"

const val NETWORK_ERROR = "Network error"
const val DURATION = "duration"

const val sArtworkUri = "content://media/external/audio/albumart"

enum class MusicState {
    PLAYING, PAUSED, NONE
}
