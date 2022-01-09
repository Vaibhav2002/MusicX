package dev.vaibhav.musicx.ui.screens.playlistScreen

import dev.vaibhav.musicx.data.models.local.Music

data class PlayListScreenState(
    val currentPlayingMusic: Music? = null,
    val sliderValue: Float = 0f,
    val isPlaying: Boolean = false,
    val prevList: List<Music> = emptyList(),
    val upcomingList: List<Music> = emptyList()
)
