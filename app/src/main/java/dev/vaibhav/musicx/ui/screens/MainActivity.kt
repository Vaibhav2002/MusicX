package dev.vaibhav.musicx.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import dev.vaibhav.musicx.ui.screens.homeScreen.HomeScreen
import dev.vaibhav.musicx.ui.screens.musicPlayer.MusicPlayerScreen
import dev.vaibhav.musicx.ui.screens.playlistScreen.PlaylistScreen
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.utils.Dispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var dispatcher: Dispatcher

//    lateinit var

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions)
        setContent {
            ProvideWindowInsets {
                MusicXTheme {
                    MainScreenUI()
                }
            }
        }
    }

    private fun askForStoragePermission() {
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalCoroutinesApi
@Composable
fun MainScreenUI() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(navController, startDestination = Screens.HomeScreen.route) {
            composable(Screens.HomeScreen.route) {
                HomeScreen {
                    navController.navigate(Screens.MusicPlayerScreen.route)
                }
            }
            composable(Screens.MusicPlayerScreen.route) {
                MusicPlayerScreen(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    navController.navigate(Screens.PlayListScreen.route)
                }
            }
            composable(Screens.PlayListScreen.route) {
                PlaylistScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
