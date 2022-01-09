package dev.vaibhav.musicx.util

import dev.vaibhav.musicx.utils.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import javax.inject.Inject

@ExperimentalCoroutinesApi
class TestDispatchers @Inject constructor() : Dispatcher {
    private val testDispatcher = StandardTestDispatcher()
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
}
