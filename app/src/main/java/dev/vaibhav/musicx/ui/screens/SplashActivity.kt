package dev.vaibhav.musicx.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicXTheme {
                SplashMainContent(viewModel = viewModel)
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToSettingsActivity() {
        startActivity(
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            }
        )
    }

    @ExperimentalPermissionsApi
    @Composable
    private fun SplashMainContent(viewModel: SplashViewModel) {
        LaunchedEffect(key1 = true) {
            viewModel.events.collectLatest {
                when (it) {
                    SplashScreenEvents.NavigateToMainActivity -> navigateToMainActivity()
                    SplashScreenEvents.NavigateToSettingsScreen -> navigateToSettingsActivity()
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_img),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .aspectRatio(1f)
            )
            if (viewModel.showPermission.value)
                Surface(modifier = Modifier.fillMaxSize()) {
                    PermissionContent(viewModel)
                }
        }
    }

    @ExperimentalPermissionsApi
    @Composable
    fun PermissionContent(viewModel: SplashViewModel) {
        val permissionState = rememberPermissionState(
            Manifest.permission.READ_EXTERNAL_STORAGE,

        )
        PermissionRequired(
            permissionState = permissionState,
            permissionNotGrantedContent = { PermissionNotGrantedContent(permissionState::launchPermissionRequest) },
            permissionNotAvailableContent = { PermissionNotAvailableContent(viewModel::onSettingsButtonPressed) }
        ) {
            viewModel.onPermissionGranted()
        }
    }

    @Composable
    fun PermissionNotGrantedContent(askPermission: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Storage permission is required to search and play music",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = askPermission) {
                Text("Allow Permission")
            }
        }
    }

    @Composable
    fun PermissionNotAvailableContent(navigateToSettingsScreen: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Storage permission denied. To search for music and play it, storage permission is needed",
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = navigateToSettingsScreen) {
                Text("Open Settings")
            }
        }
    }
}
