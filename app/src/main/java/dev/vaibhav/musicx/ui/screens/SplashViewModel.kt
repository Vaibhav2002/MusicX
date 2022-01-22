package dev.vaibhav.musicx.ui.screens

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
sealed class SplashScreenEvents {
    object NavigateToMainActivity : SplashScreenEvents()
    object NavigateToSettingsScreen : SplashScreenEvents()
}

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    val showPermission = mutableStateOf(false)

    private val _events = MutableSharedFlow<SplashScreenEvents>()
    val events = _events.asSharedFlow()

    init {
        askForPermissionAfterTime()
    }

    private fun askForPermissionAfterTime() = viewModelScope.launch {
        delay(2000L)
        showPermission.value = true
    }

    fun onPermissionGranted() = viewModelScope.launch {
        _events.emit(SplashScreenEvents.NavigateToMainActivity)
    }

    fun onSettingsButtonPressed() = viewModelScope.launch {
        _events.emit(SplashScreenEvents.NavigateToSettingsScreen)
    }
}
