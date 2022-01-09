package dev.vaibhav.musicx.ui.screens.homeScreen

import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.utils.MusicState
import dev.vaibhav.musicx.utils.MusicState.PAUSED
import dev.vaibhav.musicx.utils.MusicState.PLAYING

data class HomeScreenState(
    val musicList: List<Music> = emptyList(),
    val currentPlayingMusic: Music? = null,
    val searchBarText: String = "",
    val musicState: MusicState = MusicState.NONE
) {
    val isMusicBottomBarVisible =
        currentPlayingMusic != null && (musicState == PLAYING || musicState == PAUSED)

    val isMusicPlaying = musicState == PLAYING
}
