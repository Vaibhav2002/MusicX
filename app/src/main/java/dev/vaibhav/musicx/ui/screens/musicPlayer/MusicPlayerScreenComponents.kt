package dev.vaibhav.musicx.ui.screens.musicPlayer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.ui.components.RoundImageButton
import dev.vaibhav.musicx.ui.theme.Shapes
import dev.vaibhav.musicx.ui.utils.MUSIC_NEXT_TAG
import dev.vaibhav.musicx.ui.utils.MUSIC_PLAY_PAUSE_TAG
import dev.vaibhav.musicx.ui.utils.MUSIC_PREV_TAG
import dev.vaibhav.musicx.utils.formatDuration

data class MusicControlButtonState(
    val isPlaying: Boolean = false,
    val isPlayPauseButtonEnabled: Boolean = true,
    val isSkipPrevButtonEnabled: Boolean = true,
    val isSkipNextButtonEnabled: Boolean = true,
    val onPlayPauseButtonClicked: () -> Unit = {},
    val onSkipNextButtonPressed: () -> Unit = {},
    val onSkipPrevButtonPressed: () -> Unit = {}
)

data class MusicSliderState(
    val sliderValue: Float = 0f,
    val timePassed: Long = 0L,
    val timeLeft: Long = 0L,
    val onValueChange: (Float) -> Unit = {}
) {
    val timePassedFormatted
        get() = timePassed.formatDuration()

    val timeLeftFormatted
        get() = timeLeft.formatDuration()
}

@Composable
fun LyricsBottomSheet(text: String) {
    Surface(
        modifier = Modifier
            .fillMaxHeight(0.7f)
            .padding(horizontal = 24.dp, vertical = 24.dp),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState(), true),
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun PlayPauseButton(
    isPlaying: Boolean,
    isEnabled: Boolean = true,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    RoundImageButton(
        image = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
        iconTint = iconTint,
        backgroundColor = iconTint.copy(alpha = 0.4f),
        contentDescription = "Play/Pause Music",
        iconRelativeSize = 0.4f,
        isEnabled = isEnabled,
        modifier = Modifier
            .size(72.dp)
            .testTag(MUSIC_PLAY_PAUSE_TAG),
        iconOffset = if (isPlaying) 0.dp else 4.dp,
        onClick = onClick
    )
}

@Composable
fun PreviousTrackButton(
    iconTint: Color = MaterialTheme.colorScheme.primary,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    RoundImageButton(
        image = R.drawable.ic_round_skip_previous_24,
        iconTint = iconTint,
        isEnabled = isEnabled,
        backgroundColor = Color.Transparent,
        contentDescription = "Old Track",
        iconRelativeSize = 0.6f,
        modifier = Modifier
            .size(64.dp)
            .testTag(MUSIC_PREV_TAG),
        onClick = onClick
    )
}

@Composable
fun NextTrackButton(
    iconTint: Color = MaterialTheme.colorScheme.primary,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    RoundImageButton(
        image = R.drawable.ic_round_skip_next_24,
        iconTint = iconTint,
        isEnabled = isEnabled,
        backgroundColor = Color.Transparent,
        contentDescription = "Old Track",
        iconRelativeSize = 0.6f,
        modifier = Modifier
            .size(64.dp)
            .testTag(MUSIC_NEXT_TAG),
        onClick = onClick
    )
}

@Composable
fun PlayListButton(
    iconTint: Color = MaterialTheme.colorScheme.primary,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    RoundImageButton(
        image = R.drawable.ic_round_playlist_play_24,
        iconTint = iconTint,
        isEnabled = isEnabled,
        backgroundColor = Color.Transparent,
        contentDescription = "Old Track",
        iconRelativeSize = 0.6f,
        modifier = Modifier
            .size(44.dp),
        onClick = onClick
    )
}

@Composable
fun LoopButton(
    iconTint: Color = MaterialTheme.colorScheme.primary,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    RoundImageButton(
        image = R.drawable.ic_round_loop_24,
        iconTint = iconTint,
        isEnabled = isEnabled,
        backgroundColor = Color.Transparent,
        contentDescription = "Old Track",
        iconRelativeSize = 0.5f,
        modifier = Modifier
            .size(44.dp),
        onClick = onClick
    )
}

@Composable
fun MusicSlider(
    sliderColor: Color,
    state: MusicSliderState
) {
    Slider(
        value = state.sliderValue,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        colors = SliderDefaults.colors(
            activeTrackColor = sliderColor.copy(alpha = 0.7f),
            inactiveTrackColor = sliderColor.copy(alpha = 0.4f),
            thumbColor = sliderColor
        ),
        onValueChange = state.onValueChange,
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = state.timePassedFormatted,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary,
        )

        Text(
            text = state.timeLeftFormatted,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary,
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun MusicPoster(
    url: String,
    modifier: Modifier = Modifier
) {
    Surface(shadowElevation = 8.dp, shape = Shapes.small) {
        Image(
            painter = rememberImagePainter(data = url) {
                crossfade(true)
            },
            contentDescription = "Music Poster",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    }
}

@Composable
fun TitleArea(
    songName: String,
    artistName: String
) {
    Text(
        text = songName,
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onBackground,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(start = 16.dp)
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
        text = artistName,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun OverLappingIcons(
    icons: List<Int>,
    tint: Color,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val alphaStart = 1f - icons.size.toFloat() / 10
        icons.forEachIndexed { index, icon ->
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                tint = tint.copy(alpha = alphaStart + (index + 1f) / 10),
                modifier = Modifier
                    .fillMaxSize(0.8f + index.toFloat() / 10)
                    .padding(start = (index * 16).dp)
            )
        }
    }
}
