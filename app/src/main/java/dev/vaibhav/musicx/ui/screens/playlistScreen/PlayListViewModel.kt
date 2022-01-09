package dev.vaibhav.musicx.ui.screens.playlistScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.repo.music.MusicRepo
import dev.vaibhav.musicx.exoplayer.isPlaying
import dev.vaibhav.musicx.ui.usecases.MusicUseCase
import dev.vaibhav.musicx.utils.getMusic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayListViewModel @Inject constructor(
    private val musicUseCase: MusicUseCase,
    private val musicRepo: MusicRepo
) : ViewModel() {

    private val _uiState = mutableStateOf(PlayListScreenState())
    val uiState: State<PlayListScreenState> = _uiState

    private val allMusic =
        musicRepo.getAllSongsFlow().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val currentSong = musicUseCase.currentSong
    private val playbackState = musicUseCase.playbackState

    init {
        collectCurrentSong()
        collectTimePassed()
        collectPlaybackState()
        collectAllSongs()
    }

    private fun collectAllSongs() = viewModelScope.launch {
        allMusic.collectLatest {
            currentSong.value?.let {
                updateMusicLists(it.getMusic())
            }
        }
    }

    private fun collectPlaybackState() = viewModelScope.launch {
        playbackState.collectLatest { state ->
            state?.let {
                _uiState.value = uiState.value.copy(
                    isPlaying = it.isPlaying
                )
            }
        }
    }

    private fun collectCurrentSong() = viewModelScope.launch {
        currentSong.collectLatest {
            it?.getMusic()?.let { music ->
                _uiState.value = uiState.value.copy(currentPlayingMusic = music)
                updateMusicLists(music)
            }
        }
    }

    private fun updateMusicLists(currentSong: Music) {
        if (allMusic.value.isEmpty()) return
        val index = getMusicIndex(currentSong)
        val prevList = allMusic.value.subList(0, index)
        val upcomingList = if (index != allMusic.value.size - 1)
            allMusic.value.subList(index + 1, allMusic.value.size)
        else emptyList()
        _uiState.value = uiState.value.copy(
            prevList = prevList, upcomingList = upcomingList
        )
    }

    private fun collectTimePassed() = viewModelScope.launch {
        musicUseCase.timePassed.collectLatest {
            uiState.value.currentPlayingMusic?.let { music ->
                updateDurationInUI(it, music.duration)
            }
        }
    }

    private fun updateDurationInUI(currentTime: Long, totalTime: Long) {
        if (totalTime == 0L) return
        _uiState.value = uiState.value.copy(
            sliderValue = currentTime / totalTime.toFloat()
        )
    }

    private fun getMusicIndex(music: Music): Int {
        val index = allMusic.value.indexOfFirst { it.id == music.id }
        return if (index != -1) index else 0
    }

    fun onPlayPauseButtonPressed(music: Music) = viewModelScope.launch {
        musicUseCase.playPause(music.id, true)
    }

    fun onMusicItemPressed(music: Music) = viewModelScope.launch {
        musicUseCase.playFromMediaId(music.id)
    }

    fun onSliderValueChanged(value: Float) = viewModelScope.launch {
        _uiState.value = uiState.value.copy(sliderValue = value)
        val totalDuration = uiState.value.currentPlayingMusic?.duration ?: 0
        musicUseCase.seekTo((value * totalDuration).toLong())
    }
}
