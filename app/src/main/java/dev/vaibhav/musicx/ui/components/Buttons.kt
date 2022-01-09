package dev.vaibhav.musicx.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.ui.utils.PAUSE_MUSIC_CD
import dev.vaibhav.musicx.ui.utils.PLAY_MUSIC_CD
import dev.vaibhav.musicx.ui.utils.getPlayPauseIcon

@Composable
fun RoundImageButton(
    image: Int,
    iconTint: Color,
    iconRelativeSize: Float,
    backgroundColor: Color,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconOffset: Dp = 0.dp,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(color = backgroundColor),
        enabled = isEnabled
    ) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize(iconRelativeSize)
                .offset(x = iconOffset),
            tint = iconTint.copy(alpha = if (isEnabled) 1f else 0.5f)
        )
    }
}

@Composable
fun PlayPauseButton(
    music: Music,
    isPlaying: Boolean,
    iconRelativeSize: Float = 0.4f,
    onPlayPauseButtonPressed: (Music) -> Unit
) {
    RoundImageButton(
        image = getPlayPauseIcon(isPlaying),
        iconTint = MaterialTheme.colorScheme.tertiary,
        iconRelativeSize = iconRelativeSize,
        backgroundColor = Color.Transparent,
        contentDescription = if (isPlaying) PAUSE_MUSIC_CD else PLAY_MUSIC_CD,
        onClick = {
            onPlayPauseButtonPressed(music)
        },
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        iconOffset = if (isPlaying) 0.dp else 4.dp,
    )
}

@Preview
@Composable
fun PlayButtonPreviewLight() {
    MusicXTheme {
        RoundImageButton(
            image = R.drawable.ic_play,
            iconTint = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentDescription = "Play Music",
            iconRelativeSize = 0.4f,
            modifier = Modifier.size(72.dp),
            iconOffset = 4.dp
        ) {}
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PlayButtonPreviewDark() {
    MusicXTheme {
        RoundImageButton(
            image = R.drawable.ic_play,
            iconTint = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
            contentDescription = "Play Music",
            iconRelativeSize = 0.4f,
            modifier = Modifier.size(72.dp),
            iconOffset = 4.dp
        ) {}
    }
}
