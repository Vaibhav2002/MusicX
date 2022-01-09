package dev.vaibhav.musicx.ui.screens.musicPlayer

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.repo.lyrics.LyricsRepo
import dev.vaibhav.musicx.data.repo.music.MusicRepo
import dev.vaibhav.musicx.exoplayer.isPlaying
import dev.vaibhav.musicx.ui.usecases.MusicUseCase
import dev.vaibhav.musicx.utils.Resource
import dev.vaibhav.musicx.utils.getArtistsString
import dev.vaibhav.musicx.utils.getMusic
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val musicRepo: MusicRepo,
    private val lyricsRepo: LyricsRepo,
    private val musicUseCase: MusicUseCase
) : ViewModel() {

    private val songs =
        musicRepo.getAllSongsFlow().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _uiState = mutableStateOf(
        MusicPlayerScreenState(musicControlButtonState = getMusicControlButtonState())
    )
    val uiState: State<MusicPlayerScreenState> = _uiState

    private val playbackState = musicUseCase.playbackState
    private val currentSong = musicUseCase.currentSong

    init {
//        setMusicControlButtonState()
        setMusicSliderState()
        collectSongs()
        collectTimePassed()
        collectPlaybackState()
    }

    private fun setMusicControlButtonState() = viewModelScope.launch {
        _uiState.value = uiState.value.copy(
            musicControlButtonState = uiState.value.musicControlButtonState.copy()
        )
    }

    private fun getMusicControlButtonState() = MusicControlButtonState(
        onPlayPauseButtonClicked = this::onPlayPauseButtonPressed,
        onSkipNextButtonPressed = this::onNextTrackPressed,
        onSkipPrevButtonPressed = this::onPrevTrackPressed
    )

    private fun setMusicSliderState() = viewModelScope.launch {
        _uiState.value = uiState.value.copy(
            musicSliderState = uiState.value.musicSliderState.copy(
                onValueChange = this@MusicPlayerViewModel::onSeekBarValueChanged
            )
        )
    }

    private fun onSeekBarValueChanged(value: Float) = viewModelScope.launch {
        _uiState.value = uiState.value.copy(
            musicSliderState = uiState.value.musicSliderState.copy(sliderValue = value)
        )
        val seekTime = (value * uiState.value.totalDuration).toLong()
        musicUseCase.seekTo(seekTime)
    }

    private fun onNextTrackPressed() = viewModelScope.launch {
        musicUseCase.skipToNextTrack()
    }

    private fun onPrevTrackPressed() = viewModelScope.launch {
        musicUseCase.skipToPrevTrack()
    }

    private fun onPlayPauseButtonPressed() = viewModelScope.launch {
        currentSong.value?.let {
            musicUseCase.playPause(it.getMusic().id, true)
        }
    }

    private fun collectPlaybackState() = viewModelScope.launch {
        playbackState.collectLatest { playback ->
            playback?.let {
                _uiState.value = uiState.value.copy(
                    isPlaying = it.isPlaying,
                    musicControlButtonState = uiState.value.musicControlButtonState.copy(
                        isPlaying = it.isPlaying
                    )
                )
            }
        }
    }

    private fun collectTimePassed() = viewModelScope.launch {
        musicUseCase.timePassed.collectLatest {
            updateDurationInUI(it, uiState.value.totalDuration)
        }
    }

    private fun collectSongs() = viewModelScope.launch {
        songs.collectLatest {
            val oldCount = uiState.value.songsCount
            _uiState.value = uiState.value.copy(allSongs = it)
            if (oldCount == 0 && it.isNotEmpty()) {
                collectCurrentSong()
            }
        }
    }

    private fun collectCurrentSong() = viewModelScope.launch {
        currentSong.collectLatest { song ->
            song?.let { metadata ->
                val music = metadata.getMusic()
                fetchLyrics(music)
                val songIndex = songs.value.indexOf(music)
                _uiState.value = uiState.value.copy(
                    image = music.imageUrl,
                    songName = music.title,
                    songArtistName = music.artists.getArtistsString(),
                    currentSongIndex = if (songIndex != -1) songIndex else 0,
                    totalDuration = music.duration,
                    musicControlButtonState = uiState.value.musicControlButtonState.copy(
                        isSkipNextButtonEnabled = songs.value.indexOfFirst { it.id == music.id } != songs.value.size - 1,
                        isSkipPrevButtonEnabled = songs.value.indexOfFirst { it.id == music.id } != 0
                    )
                )
            }
        }
    }

    private fun fetchLyrics(song: Music) = viewModelScope.launch {
        lyricsRepo.getLyric(song).collectLatest {
            val lyrics = when (it) {
                is Resource.Success -> it.data?.lyrics ?: ""
                else -> ""
            }
            _uiState.value = uiState.value.copy(lyrics = lyrics)
        }
    }

    private fun updateDurationInUI(currentTime: Long, totalTime: Long) {
        if (totalTime == 0L) return
        _uiState.value = uiState.value.copy(
            musicSliderState = uiState.value.musicSliderState.copy(
                timePassed = currentTime,
                timeLeft = totalTime - currentTime,
                sliderValue = currentTime / totalTime.toFloat()
            )
        )
    }

    suspend fun calcGradientColor(
        drawable: Drawable,
        isDarkMode: Boolean,
    ): Pair<Color, Color>? = suspendCoroutine { cont ->
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            cont.resume(getColorFromPalette(palette, isDarkMode))
        }
    }

    suspend fun calcControlsColor(
        drawable: Drawable
    ): Color? = suspendCoroutine { cont ->
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bitmap).generate { palette ->
            cont.resume(palette?.vibrantSwatch?.rgb?.let { Color(it) })
        }
    }

    private fun getColorFromPalette(palette: Palette?, isDarkMode: Boolean): Pair<Color, Color>? =
        if (isDarkMode)
            getColorForDarkMode(palette)
        else
            getColorForLightMode(palette)

    private fun getColorForDarkMode(palette: Palette?): Pair<Color, Color>? =
        palette?.darkVibrantSwatch?.let { getColorPairFromSwatch(it) }
            ?: palette?.vibrantSwatch?.let { getColorPairFromSwatch(it) }

    private fun getColorForLightMode(palette: Palette?): Pair<Color, Color>? =
        palette?.lightVibrantSwatch?.let { getColorPairFromSwatch(it) }
            ?: palette?.vibrantSwatch?.let { getColorPairFromSwatch(it) }

    private fun getColorPairFromSwatch(swatch: Palette.Swatch): Pair<Color, Color> =
        Pair(Color(swatch.rgb), Color(swatch.titleTextColor))
}
