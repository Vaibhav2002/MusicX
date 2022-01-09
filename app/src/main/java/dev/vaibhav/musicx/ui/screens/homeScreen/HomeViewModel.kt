package dev.vaibhav.musicx.ui.screens.homeScreen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.repo.music.MusicRepo
import dev.vaibhav.musicx.ui.usecases.MusicUseCase
import dev.vaibhav.musicx.utils.Dispatcher
import dev.vaibhav.musicx.utils.MusicState
import dev.vaibhav.musicx.utils.getMusic
import dev.vaibhav.musicx.utils.getMusicState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val musicRepo: MusicRepo,
    private val musicUseCase: MusicUseCase,
    private val dispatcher: Dispatcher
) : ViewModel() {

    private val _uiState = mutableStateOf(HomeScreenState())
    val uiState: State<HomeScreenState> = _uiState

    private val _navigateToMusicScreen = MutableSharedFlow<Boolean>()
    val navigateToMusicScreen = _navigateToMusicScreen.asSharedFlow()

    private val searchQuery = MutableStateFlow("")

    private val musicList = searchQuery.flatMapLatest {
        musicRepo.getAllSongsFlow(it)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

//    private val musicList = searchQuery.flatMapLatest { query ->
//        musicRepo.getAllSongsFlow().map { items -> items.filter { it.title.contains(query) } }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val currentSong = musicUseCase.currentSong
    private val playBackState = musicUseCase.playbackState

    init {
//        sampleMusicList.subList(3, sampleMusicList.size).forEach {
//            uploadMusic(it)
//        }
        collectSongs()
        collectCurrentSong()
        collectPlayBackState()
    }

    private fun uploadMusic(music: Music) = viewModelScope.launch(dispatcher.main) {
        musicRepo.uploadSong(music).collectLatest {
            Timber.d("${music.title} - ${it.javaClass}")
        }
    }

    private fun collectSongs() = viewModelScope.launch(dispatcher.main) {
        musicList.collectLatest {
            _uiState.value = uiState.value.copy(musicList = it)
        }
    }

    private fun collectCurrentSong() = viewModelScope.launch(dispatcher.main) {
        currentSong.collectLatest {
            val music = it?.getMusic()
            _uiState.value = uiState.value.copy(currentPlayingMusic = music)
        }
    }

    private fun collectPlayBackState() = viewModelScope.launch(dispatcher.main) {
        playBackState.collectLatest { playback ->
            val musicState = playback?.getMusicState() ?: MusicState.NONE
            _uiState.value = uiState.value.copy(musicState = musicState)
        }
    }

    fun onMusicListItemPressed(music: Music) = viewModelScope.launch(dispatcher.main) {
        musicUseCase.playPause(music.id)
    }

    fun onPlayPauseButtonPressed(music: Music) = viewModelScope.launch(dispatcher.main) {
        musicUseCase.playPause(music.id, true)
    }

    fun onMusicBottomBarPressed(music: Music) = viewModelScope.launch(dispatcher.main) {
        _navigateToMusicScreen.emit(true)
    }

    fun onSearchQueryChanged(query: String) = viewModelScope.launch(dispatcher.main) {
        _uiState.value = uiState.value.copy(searchBarText = query)
        searchQuery.emit(query)
    }
}
