package dev.vaibhav.musicx.utils

import android.content.Context
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import dev.vaibhav.musicx.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@LargeTest
class ExtensionsKtTest {

    private lateinit var context: Context
    private lateinit var dispatcher: Dispatcher

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
        dispatcher = TestDispatchers()
    }

    @Test
    fun songsAreLoadedCorrectly() = runTest {
        val musicList = context.getLocalMusicList(dispatcher).first()
        println(musicList)
        assertThat(musicList).isNotEmpty()
    }
}
