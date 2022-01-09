package dev.vaibhav.musicx.data.models.mapper

import dev.vaibhav.musicx.data.models.local.Lyric
import dev.vaibhav.musicx.data.models.remote.lyrics.LyricsDTO
import javax.inject.Inject

class LyricMapper @Inject constructor() : Mapper<LyricsDTO, Lyric> {
    override fun toDomain(network: LyricsDTO): Lyric = Lyric(
        artists = network.artist,
        fullTitle = network.fullTitle,
        lyrics = network.lyrics,
        songId = "",
        title = network.title
    )

    override fun toDomainList(networks: List<LyricsDTO>): List<Lyric> = networks.map {
        toDomain(it)
    }

    override fun toNetwork(domain: Lyric): LyricsDTO = LyricsDTO(
        artist = domain.artists,
        fullTitle = domain.fullTitle,
        lyrics = domain.lyrics,
        songId = "",
        title = domain.title
    )

    override fun toNetworkList(domains: List<Lyric>): List<LyricsDTO> = domains.map {
        toNetwork(it)
    }
}
