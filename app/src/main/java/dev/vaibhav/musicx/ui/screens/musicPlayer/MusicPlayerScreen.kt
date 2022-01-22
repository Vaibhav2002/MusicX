package dev.vaibhav.musicx.ui.screens.musicPlayer

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.Coil
import coil.request.ImageRequest
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MusicPlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicPlayerViewModel = hiltViewModel(),
    onPlayListButtonPress: () -> Unit
) {
    val context = LocalContext.current
    val bottomColor = MaterialTheme.colorScheme.background
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val tempDominantColor = remember { mutableStateOf(bottomColor) }
    val dominantColor = remember { Animatable(bottomColor) }
    val tempControlColor = remember { mutableStateOf(primaryColor) }
    val controlsColor = remember { Animatable(primaryColor) }
    val onDominantColor = remember { Animatable(primaryColor) }
    val tempOnDominantColor = remember { mutableStateOf(primaryColor) }
    val isDarkMode = isSystemInDarkTheme()
    LaunchedEffect(key1 = tempControlColor.value) {
        controlsColor.animateTo(tempControlColor.value)
    }
    LaunchedEffect(key1 = tempDominantColor.value) {
        dominantColor.animateTo(tempDominantColor.value)
    }
    LaunchedEffect(key1 = tempOnDominantColor) {
        onDominantColor.animateTo(tempOnDominantColor.value)
    }
    LaunchedEffect(key1 = viewModel.uiState.value.image) {
        setGradientColorFromImage(
            url = viewModel.uiState.value.image,
            context = context,
            isDarkMode = isDarkMode,
            viewModel = viewModel
        ) {
            Timber.d(it.toString())
            tempDominantColor.value = it?.first ?: primaryColor
            tempOnDominantColor.value = it?.second ?: onPrimaryColor
        }

        setControlsColorFromImage(
            url = viewModel.uiState.value.image,
            context = context,
            viewModel = viewModel
        ) {
            tempControlColor.value = it
        }
    }
    if (viewModel.uiState.value.isBottomSheetVisible)
        MusicMainContentWithBottomSheet(
            modifier = modifier,
            viewModel = viewModel,
            dominantColor = dominantColor.value,
            onDominantColor = onDominantColor.value,
            controlsColor = controlsColor.value,
            bottomColor = bottomColor,
            onPlayListButtonPress
        )
    else
        MusicMainContent(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            dominantColor = dominantColor.value,
            controlsColor = controlsColor.value,
            bottomColor = bottomColor,
            onPlayListButtonPress
        )
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun MusicMainContentWithBottomSheet(
    modifier: Modifier,
    viewModel: MusicPlayerViewModel,
    dominantColor: Color,
    onDominantColor: Color,
    controlsColor: Color,
    bottomColor: Color,
    onPlayListButtonPress: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        modifier = modifier.navigationBarsPadding(),
        scaffoldState = scaffoldState,
        sheetBackgroundColor = dominantColor,
        sheetContentColor = onDominantColor,
        sheetPeekHeight = 48.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        sheetElevation = 16.dp,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        sheetGesturesEnabled = true,
        sheetContent = {
//            LyricsBottomSheet(
//                text = viewModel.uiState.value.lyrics,
//            )
            Text(
                text = viewModel.uiState.value.lyrics,
                modifier = Modifier
                    .fillMaxHeight(0.6f)
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState(), true),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            BackHandler(enabled = scaffoldState.bottomSheetState.isExpanded) {
                coroutineScope.launch { scaffoldState.bottomSheetState.collapse() }
            }
        }
    ) {
        MusicMainContent(
            modifier = Modifier.fillMaxSize(),
            viewModel,
            dominantColor,
            controlsColor,
            bottomColor,
            onPlayListButtonPress
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun MusicMainContent(
    modifier: Modifier = Modifier,
    viewModel: MusicPlayerViewModel,
    dominantColor: Color,
    controlsColor: Color,
    bottomColor: Color,
    onPlayListButtonPress: () -> Unit
) {
    Column(modifier = modifier) {

//        LaunchedEffect(key1 = viewModel.uiState.value.currentSongIndex) {
//            pagerState.animateScrollToPage(viewModel.uiState.value.currentSongIndex)
//        }
        // box for gradient and image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(dominantColor, bottomColor)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            val isMusicPosterVisible by remember { mutableStateOf(true) }
            androidx.compose.animation.AnimatedVisibility(
                visible = isMusicPosterVisible,
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
            ) {
                MusicPoster(
                    url = viewModel.uiState.value.image,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        TitleArea(
            songName = viewModel.uiState.value.songName,
            artistName = viewModel.uiState.value.songArtistName,
        )
        Spacer(modifier = Modifier.height(48.dp))
        MusicSlider(
            sliderColor = controlsColor,
            state = viewModel.uiState.value.musicSliderState
        )
        Spacer(modifier = Modifier.height(32.dp))
        MusicControlButtons(
            tint = controlsColor,
            viewModel.uiState.value.musicControlButtonState,
            onPlayListButtonPress
        )
    }
}

@Composable
private fun MusicControlButtons(
    tint: Color,
    state: MusicControlButtonState,
    onPlayListButtonPress: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        LoopButton(
            tint,
            true,
        ) {}
        PreviousTrackButton(
            tint,
            true,
            state.onSkipPrevButtonPressed
        )
//        Spacer(modifier = Modifier.width(32.dp))
        PlayPauseButton(
            state.isPlaying,
            state.isPlayPauseButtonEnabled,
            tint,
            state.onPlayPauseButtonClicked
        )
//        Spacer(modifier = Modifier.width(32.dp))
        NextTrackButton(
            tint,
            state.isSkipNextButtonEnabled,
            state.onSkipNextButtonPressed
        )
//        Spacer(modifier = Modifier.width(32.dp))
        PlayListButton(tint, true, onPlayListButtonPress)
    }
}

private suspend fun setGradientColorFromImage(
    url: String,
    context: Context,
    isDarkMode: Boolean,
    viewModel: MusicPlayerViewModel,
    onImageLoaded: (Pair<Color, Color>?) -> Unit
) {
    Coil.execute(ImageRequest.Builder(context).data(url).build()).drawable?.let {
//        Timber.d(viewModel.calcGradientColor(it, isDarkMode).toString())
        viewModel.calcGradientColor(it, isDarkMode).let(onImageLoaded)
    }
}

private suspend fun setControlsColorFromImage(
    url: String,
    context: Context,
    viewModel: MusicPlayerViewModel,
    onImageLoaded: (Color) -> Unit
) {
    Coil.execute(ImageRequest.Builder(context).data(url).build()).drawable?.let {
        viewModel.calcControlsColor(it)?.let(onImageLoaded)
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(
    name = "ImageLight",
    uiMode = UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun PreviewImageLight() {
    MusicXTheme {
        MusicPlayerScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {}
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(
    name = "ImageDark",
    uiMode = UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun PreviewImage() {
    MusicXTheme {
        MusicPlayerScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {}
    }
}
