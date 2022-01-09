package dev.vaibhav.musicx.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.vaibhav.musicx.utils.AppDispatcher
import dev.vaibhav.musicx.utils.Dispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineModule {

    @Singleton
    @Binds
    abstract fun bindsCoroutineDispatcher(
        appCoroutineDispatcher: AppDispatcher
    ): Dispatcher
}
