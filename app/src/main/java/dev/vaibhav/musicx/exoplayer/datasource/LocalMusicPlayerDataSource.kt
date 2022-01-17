package dev.vaibhav.musicx.exoplayer.datasource

import android.content.Context
import dev.vaibhav.musicx.utils.Dispatcher
import dev.vaibhav.musicx.utils.getLocalMusicList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalMusicPlayerDataSource @Inject constructor(
    private val context: Context,
    private val dispatcher: Dispatcher
) : MusicPlayerDataSource() {

    override suspend fun getMusic() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        allMusic = context.getLocalMusicList(dispatcher).first()
        state = State.STATE_INITIALIZED
    }
}
