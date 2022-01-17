package dev.vaibhav.musicx.di

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dev.vaibhav.musicx.exoplayer.datasource.LocalMusicPlayerDataSource
import dev.vaibhav.musicx.exoplayer.datasource.MusicPlayerDataSource

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun providesAudioAttributes() = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @ServiceScoped
    @Provides
    fun providesExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .build().apply {
            setThrowsWhenUsingWrongThread(false)
        }

    @ServiceScoped
    @Provides
    fun providesDataSourceFactor(
        @ApplicationContext context: Context
    ): DefaultDataSource.Factory = DefaultDataSource.Factory(context)

//    @ServiceScoped
//    @Provides
//    fun providesMusicPlayerDataSource(
//        dataSource: MusicDataSource,
//        musicMapper: MusicMapper,
//        musicDao: MusicDao
//    ): MusicPlayerDataSource = FirebaseMusicPlayerDataSource(dataSource, musicMapper, musicDao)

//    @ServiceScoped
//    @Provides
//    fun providesMusicDb(
//        @ApplicationContext context: Context
//    ): MusicXDatabase = Room.databaseBuilder(context, MusicXDatabase::class.java, "MusicXDatabase")
//        .fallbackToDestructiveMigration()
//        .build()
//
//    @ServiceScoped
//    @Provides
//    fun providesMusicDao(database: MusicXDatabase): MusicDao = database.getMusicDao()
}

@Module
@InstallIn(ServiceComponent::class)
abstract class ServiceInterfaces {

    @ServiceScoped
    @Binds
    abstract fun bindsMusicDataSource(
        localMusicPlayerDataSource: LocalMusicPlayerDataSource
    ): MusicPlayerDataSource
}
