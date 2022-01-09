package dev.vaibhav.musicx.ui.utils

import dev.vaibhav.musicx.R

fun getPlayPauseIcon(isPlaying: Boolean) =
    if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play

const val PLAY_MUSIC_CD = "Play music"
const val PAUSE_MUSIC_CD = "Pause music"
