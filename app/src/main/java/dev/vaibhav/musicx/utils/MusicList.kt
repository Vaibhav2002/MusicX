package dev.vaibhav.musicx.utils

import dev.vaibhav.musicx.data.models.local.Music
import java.util.*

val sampleMusicList = listOf(
    Music(
        id = UUID.randomUUID().toString(),
        title = "Sunflower - Spider-Man: Into the Spider-Verse",
        artists = listOf("Post Malone", "Swae Lee"),
        imageUrl = "https://i.scdn.co/image/ab67616d00001e02e2e352d89826aef6dbd5ff8f",
        musicUrl = "",
        duration = 158000
    ),
    Music(
        id = UUID.randomUUID().toString(),
        title = "I'm Ready",
        artists = listOf("Jaden"),
        imageUrl = "https://i.scdn.co/image/ab67616d00001e026094c28a79dc47837fd269d9",
        musicUrl = "",
        duration = 189000
    ),
    Music(
        id = UUID.randomUUID().toString(),
        title = "Rap God",
        artists = listOf("Eminem"),
        imageUrl = "https://i.scdn.co/image/ab67616d00004851643e6ecebab400d52574e4b2",
        musicUrl = "",
        duration = 363000
    ),

    // data set 2
    Music(
        id = UUID.randomUUID().toString(),
        title = "Ranjha - Shershaah",
        artists = listOf("Jasleen Royal", "B Praak"),
        imageUrl = "https://i.scdn.co/image/ab67616d00001e0209426d9ae9d8d981735ebc5e",
        musicUrl = "",
        duration = (3 * 60 + 48) * 1000L
    ),
    Music(
        id = UUID.randomUUID().toString(),
        title = "Tune Jo Na Kaha",
        artists = listOf("Pritam", "Mohit Chauhan", "Sandeep Shrivastava"),
        imageUrl = "https://i.scdn.co/image/ab67616d00001e02d567bc615d8d891d112c8a35",
        musicUrl = "",
        duration = (5 * 60 + 10) * 1000L
    ),
    Music(
        id = UUID.randomUUID().toString(),
        title = "Same Beef",
        artists = listOf("Bohemia", "Siddhu Moosewala"),
        imageUrl = "https://i.scdn.co/image/ab67616d00001e02d6181999e3c1c1bb525f9989",
        musicUrl = "",
        duration = (4 * 60 + 22) * 1000L
    )
)
