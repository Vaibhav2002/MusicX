package dev.vaibhav.musicx.ui.screens.playlistScreen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.ui.components.PlayPauseButton
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.utils.getArtistsString
import dev.vaibhav.musicx.utils.sampleMusicList

@Composable
fun CurrentlyPlayingItem(
    music: Music,
    isPlaying: Boolean,
    progress: Float,
    sliderColor: Color,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    onPlayPauseButtonPress: (Music) -> Unit,
    onSliderValueChange: (Float) -> Unit
) {
    Box(modifier = Modifier.wrapContentHeight()) {
        Surface(
            onClick = {},
            shape = shape,
            modifier = modifier,
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(music.imageUrl),
                    contentDescription = "Music cover",
                    modifier = Modifier
                        .aspectRatio(1f),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                PlayPauseButton(
                    music = music,
                    iconRelativeSize = 0.3f,
                    isPlaying = isPlaying,
                    onPlayPauseButtonPressed = onPlayPauseButtonPress
                )
            }
        }

        Slider(
            value = progress,
            onValueChange = onSliderValueChange,
            colors = SliderDefaults.colors(
                activeTrackColor = sliderColor.copy(alpha = 0.7f),
                inactiveTrackColor = sliderColor.copy(alpha = 0.4f),
                thumbColor = sliderColor
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(top = 56.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
fun CurrentlyPlayingPrev() {
    MusicXTheme {
        CurrentlyPlayingItem(
            music = sampleMusicList[0],
            isPlaying = true,
            sliderColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            onPlayPauseButtonPress = {},
            progress = 0.43f,
            onSliderValueChange = {}
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun CurrentlyPlayingPrevDark() {
    MusicXTheme {
        CurrentlyPlayingItem(
            music = sampleMusicList[0],
            isPlaying = true,
            sliderColor = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            onPlayPauseButtonPress = {},
            progress = 0.68f,
            onSliderValueChange = {}
        )
    }
}
