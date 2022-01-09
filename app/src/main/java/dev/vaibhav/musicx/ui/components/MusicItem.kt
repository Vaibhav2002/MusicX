package dev.vaibhav.musicx.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.utils.getArtistsString
import dev.vaibhav.musicx.utils.sampleMusicList

@Composable
fun MusicItem(
    music: Music,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(8.dp),
    onItemClick: (Music) -> Unit
) {
    Surface(
        onClick = {
            onItemClick(music)
        },
        shape = shape,
        modifier = modifier,
        color = color,
        tonalElevation = 1.dp,
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
                    .fillMaxWidth()
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
        }
    }
}

// Previews
@Preview
@Composable
private fun MusicItemPreview() {
    MusicXTheme {
        MusicItem(
            music = sampleMusicList[0],
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MusicItemPreviewDark() {
    MusicXTheme {
        MusicItem(
            music = sampleMusicList[0],
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MusicListLight() {
    MusicXTheme {
        LazyColumn {
            items(sampleMusicList) {
                MusicItem(
                    music = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun MusicListDark() {
    MusicXTheme {
        LazyColumn {
            items(sampleMusicList) {
                MusicItem(
                    music = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}
