package dev.vaibhav.musicx.exoplayer.datasource

import android.content.Context
import dev.vaibhav.musicx.data.local.dataSource.RoomMusicDataSource
import dev.vaibhav.musicx.utils.Dispatcher
import dev.vaibhav.musicx.utils.getLocalMusicList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalMusicPlayerDataSource @Inject constructor(
    private val context: Context,
    private val roomDataSource: RoomMusicDataSource,
    private val dispatcher: Dispatcher
) : MusicPlayerDataSource() {

    override suspend fun getMusic() = withContext(Dispatchers.IO) {
        state = State.STATE_INITIALIZING
        allMusic = context.getLocalMusicList(dispatcher)
        roomDataSource.insertSong(allMusic)
        state = State.STATE_INITIALIZED
    }
}
