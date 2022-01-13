package dev.vaibhav.musicx.ui.screens

import android.annotation.SuppressLint
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vaibhav.musicx.data.repo.music.MusicRepo
import dev.vaibhav.musicx.ui.usecases.MusicUseCase
import dev.vaibhav.musicx.utils.Dispatcher
import dev.vaibhav.musicx.utils.Resource
import dev.vaibhav.musicx.utils.getMusic
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicUseCase: MusicUseCase,
    private val musicRepo: MusicRepo,
    private val dispatcher: Dispatcher,
) : ViewModel() {

    init {
        subscribeToMusic()
        downloadMusic()
    }

    private fun subscribeToMusic() = viewModelScope.launch {
        val resource = musicUseCase.subscribeToService()
        if (resource is Resource.Success)
            handleMusic(resource.data ?: emptyList())
    }

    private fun handleMusic(metaDataList: List<MediaBrowserCompat.MediaItem>) =
        viewModelScope.launch(dispatcher.main) {
            val music = metaDataList.map { it.getMusic() }
            musicRepo.cacheMusic(music)
        }

    override fun onCleared() {
        super.onCleared()
        musicUseCase.unsubscribeToService()
    }

    private fun downloadMusic() {
    }
}
