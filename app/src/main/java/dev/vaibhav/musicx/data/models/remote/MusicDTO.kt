package dev.vaibhav.musicx.data.models.remote

data class MusicDTO(
    val id: String = "",
    val title: String = "",
    val duration: Long = 0L,
    val artists: List<String> = emptyList(),
    val imageUrl: String = "",
    val musicUrl: String = ""
)
