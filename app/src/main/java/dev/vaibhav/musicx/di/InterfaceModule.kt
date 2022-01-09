package dev.vaibhav.musicx.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vaibhav.musicx.data.remote.dataSource.lyrics.LyricDataSource
import dev.vaibhav.musicx.data.remote.dataSource.lyrics.LyricsDataSourceImpl
import dev.vaibhav.musicx.data.remote.dataSource.music.FirebaseMusicDataSource
import dev.vaibhav.musicx.data.remote.dataSource.music.MusicDataSource
import dev.vaibhav.musicx.data.repo.lyrics.LyricsRepo
import dev.vaibhav.musicx.data.repo.lyrics.LyricsRepoImpl
import dev.vaibhav.musicx.data.repo.music.MusicRepo
import dev.vaibhav.musicx.data.repo.music.MusicRepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Binds
    abstract fun bindsMusicDataSource(
        musicDataSourceImpl: FirebaseMusicDataSource
    ): MusicDataSource

    @Binds
    abstract fun bindsMusicRepo(
        musicRepoImpl: MusicRepoImpl
    ): MusicRepo

    @Binds
    abstract fun bindsLyricDataSource(
        lyricsDataSourceImpl: LyricsDataSourceImpl
    ): LyricDataSource

    @Singleton
    @Binds
    abstract fun bindsLyricsRepo(
        lyricsRepoImpl: LyricsRepoImpl
    ): LyricsRepo
}
