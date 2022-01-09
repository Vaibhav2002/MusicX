package dev.vaibhav.musicx.data.models.remote.lyrics

import com.google.gson.annotations.SerializedName

data class LyricsDTO(
    @SerializedName("artist")
    val artist: String = "",
    @SerializedName("artist_id")
    val artistId: String = "",
    @SerializedName("full_title")
    val fullTitle: String = "",
    @SerializedName("lyrics")
    val lyrics: String = "",
    @SerializedName("media")
    val media: String = "",
    @SerializedName("song_id")
    val songId: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_with_featured")
    val titleWithFeatured: String = ""
)
