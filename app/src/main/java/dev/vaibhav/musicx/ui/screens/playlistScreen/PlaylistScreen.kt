package dev.vaibhav.musicx.ui.screens.playlistScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.statusBarsPadding
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.ui.components.MusicItem
import dev.vaibhav.musicx.ui.theme.MusicXTheme

@ExperimentalFoundationApi
@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlayListViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.value
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(
                modifier = Modifier
                    .statusBarsPadding()
                    .height(40.dp)
            )
            uiState.currentPlayingMusic?.let {
                ScreenHeader(viewModel)
            }
        }
        playlist(
            previous = uiState.prevList,
            upcoming = uiState.upcomingList,
            onMusicPress = viewModel::onMusicItemPressed
        )
    }
}

@Composable
private fun ScreenHeader(viewModel: PlayListViewModel) {
    Text(
        text = stringResource(id = R.string.playlist),
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(24.dp))
    viewModel.uiState.value.currentPlayingMusic?.let {
        CurrentlyPlayingItem(
            music = it,
            isPlaying = viewModel.uiState.value.isPlaying,
            progress = viewModel.uiState.value.sliderValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            sliderColor = MaterialTheme.colorScheme.tertiary,
            onPlayPauseButtonPress = viewModel::onPlayPauseButtonPressed,
            onSliderValueChange = viewModel::onSliderValueChanged
        )
    }
}

@ExperimentalFoundationApi
fun LazyListScope.playlist(
    previous: List<Music>,
    upcoming: List<Music>,
    modifier: Modifier = Modifier,
    onMusicPress: (Music) -> Unit
) {
    val map = mapOf("Previous" to previous, "Upcoming" to upcoming)
    map.entries.toList().forEach {
        if (it.value.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                PlayListHeader(text = it.key)
                Spacer(modifier = Modifier.height(16.dp))
            }
            items(it.value) { music ->
                MusicItem(
                    music = music, onItemClick = onMusicPress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun PlayListHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@ExperimentalFoundationApi
@Preview
@Composable
fun PlayListScreenPrev() {
    MusicXTheme {
        PlaylistScreen(modifier = Modifier.fillMaxWidth())
    }
}
