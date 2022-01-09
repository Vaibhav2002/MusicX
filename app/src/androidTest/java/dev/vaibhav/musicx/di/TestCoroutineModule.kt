package dev.vaibhav.musicx.di

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.vaibhav.musicx.util.TestDispatchers
import dev.vaibhav.musicx.utils.Dispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CoroutineModule::class]
)
@Module
abstract class TestCoroutineModule {

    @ExperimentalCoroutinesApi
    @Binds
    @Singleton
    abstract fun bindsCoroutineDispatcher(
        testDispatchers: TestDispatchers
    ): Dispatcher
}
