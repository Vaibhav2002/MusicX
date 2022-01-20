package dev.vaibhav.musicx.data.models.mapper

import androidx.core.net.toUri
import dev.vaibhav.musicx.data.models.local.Music
import dev.vaibhav.musicx.data.models.remote.MusicDTO
import javax.inject.Inject

class MusicMapper @Inject constructor() : Mapper<MusicDTO, Music> {
    override fun toDomain(network: MusicDTO): Music = Music(
        id = network.id,
        title = network.title,
        duration = network.duration,
        artists = network.artists,
        imageUrl = network.imageUrl,
        musicUrl = network.musicUrl.toUri()
    )

    override fun toDomainList(networks: List<MusicDTO>): List<Music> = networks.map {
        toDomain(it)
    }

    override fun toNetwork(domain: Music): MusicDTO = MusicDTO(
        id = domain.id,
        title = domain.title,
        duration = domain.duration,
        artists = domain.artists,
        imageUrl = domain.imageUrl,
        musicUrl = domain.musicUrl.toString()
    )

    override fun toNetworkList(domains: List<Music>): List<MusicDTO> = domains.map {
        toNetwork(it)
    }
}
