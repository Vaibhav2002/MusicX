package dev.vaibhav.musicx.data.models.remote.lyrics

import com.google.gson.annotations.SerializedName

data class LyricsResponse(
    @SerializedName("result")
    val result: List<LyricsDTO> = listOf(),
    @SerializedName("status")
    val status: String = ""
)
