package dev.vaibhav.musicx.ui.screens.homeScreen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.ui.components.CoilImage
import dev.vaibhav.musicx.ui.components.PlayPauseButton
import dev.vaibhav.musicx.ui.components.SearchBar
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.ui.utils.BOTTOM_BAR_TAG
import dev.vaibhav.musicx.utils.getArtistsString

@ExperimentalComposeUiApi
@Composable
fun MusicSearchBar(
    query: String,
    modifier: Modifier = Modifier,
    onQueryChanged: (String) -> Unit
) {
    SearchBar(
        searchQuery = query,
        modifier = modifier,
        onSearchQueryChanged = onQueryChanged
    )
}

@Composable
fun MusicBottomBar(
    music: Music,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (Music) -> Unit,
    onPlayPauseButtonPressed: (Music) -> Unit
) {
    Surface(
        onClick = { onItemClick(music) },
        tonalElevation = 4.dp,
        shadowElevation = 4.dp,
        modifier = modifier.testTag(BOTTOM_BAR_TAG),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            CoilImage(
                url = music.imageUrl,
                contentDescription = "MusicPoster",
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = music.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(0.dp))
                Text(
                    text = music.artists.getArtistsString(),
                    style = MaterialTheme.typography.labelSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            PlayPauseButton(
                music = music,
                isPlaying = isPlaying,
                onPlayPauseButtonPressed = onPlayPauseButtonPressed
            )
        }
    }
}

@Preview
@Composable
fun MusicBottomBarPreviewLight() {
    MusicXTheme {
        MusicBottomBar(
            music = Music("", "Divide", 122131, listOf("Ed Sheeran"), "", ""),
            isPlaying = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onItemClick = {}
        ) {
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun MusicBottomBarPreviewDark() {
    MusicXTheme {
        MusicBottomBar(
            music = Music("", "Divide", 122131, listOf("Ed Sheeran"), "", ""),
            isPlaying = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            onItemClick = {}
        ) {
        }
    }
}
