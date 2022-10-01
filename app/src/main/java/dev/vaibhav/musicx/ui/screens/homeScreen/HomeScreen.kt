package dev.vaibhav.musicx.ui.screens.homeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.ui.components.MusicItem
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.ui.utils.HOME_SCREEN_GREETING
import dev.vaibhav.musicx.ui.utils.getMusicItemTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToPlayer: () -> Unit
) {
    val uiState by viewModel.uiState
    CollectEvents(event = viewModel.navigateToMusicScreen, navigateToPlayer)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .statusBarsPadding()
                    .height(72.dp)
            )
            Text(
                text = stringResource(id = R.string.home_header),
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag(HOME_SCREEN_GREETING)
            )
            Spacer(modifier = Modifier.height(32.dp))
            MusicSearchBar(
                query = uiState.searchBarText,
                modifier = Modifier
                    .fillMaxWidth(),
                onQueryChanged = viewModel::onSearchQueryChanged
            )
            Spacer(modifier = Modifier.height(32.dp))
            MusicList(
                musicList = uiState.musicList,
                modifier = Modifier
                    .fillMaxSize(),
                onClick = {
                    viewModel.onMusicListItemPressed(it)
                },
            )
        }
        FabWithBottomBar(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsWithImePadding()
                .padding(bottom = 16.dp)
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
private fun FabWithBottomBar(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {

        val swipeToDismissState = rememberDismissState { dismissValue ->
            if (dismissValue == DismissValue.DismissedToEnd || dismissValue == DismissValue.DismissedToStart)
                viewModel.onBottomBarDismissed()
            true
        }
        LaunchedEffect(key1 = viewModel.uiState.value.currentPlayingMusic) {
            swipeToDismissState.reset()
        }
        SwipeToDismiss(
            state = swipeToDismissState,
            background = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            dismissThresholds = { FractionalThreshold(0.7f) }
        ) {
            MusicBar(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
    }
}

@ExperimentalCoroutinesApi
@ExperimentalAnimationApi
@Composable
private fun MusicBar(viewModel: HomeViewModel, modifier: Modifier) {
    val uiState = viewModel.uiState.value
    AnimatedVisibility(
        visible = uiState.isMusicBottomBarVisible,
        enter = scaleIn(),
        exit = ExitTransition.None,
        modifier = modifier
    ) {
        MusicBottomBar(
            music = uiState.currentPlayingMusic!!,
            isPlaying = uiState.isMusicPlaying,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onPlayPauseButtonPressed = viewModel::onPlayPauseButtonPressed,
            onItemClick = viewModel::onMusicBottomBarPressed
        )
    }
}

@Composable
fun CollectEvents(event: SharedFlow<Boolean>, navigateToPlayer: () -> Unit) {
    LaunchedEffect(key1 = true) {
        event.collectLatest {
            if (it) navigateToPlayer()
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun MusicList(
    musicList: List<Music>,
    modifier: Modifier = Modifier,
    onClick: (Music) -> Unit
) {
    LazyColumn(modifier = modifier) {
        musicList.groupBy { it.title.first() }.toSortedMap().forEach {
            stickyHeader {
                MusicListHeader(text = it.key.toString())
            }
            itemsIndexed(it.value) { index, music ->
                MusicItem(
                    music = music,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(getMusicItemTag(index))
                        .height(64.dp),
                    color = MaterialTheme.colorScheme.surface,
                    onItemClick = onClick
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun MusicListHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    )
}

@ExperimentalMaterialApi
@ExperimentalCoroutinesApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    MusicXTheme {
        HomeScreen {
        }
    }
}
