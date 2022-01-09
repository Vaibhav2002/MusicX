package dev.vaibhav.musicx.data.remote.dataSource.music

import com.google.firebase.firestore.FirebaseFirestore
import dev.vaibhav.musicx.data.models.remote.MusicDTO
import dev.vaibhav.musicx.data.remote.util.FirestoreKeys.MUSIC_KEY
import dev.vaibhav.musicx.utils.Resource
import dev.vaibhav.musicx.utils.mapToUnit
import dev.vaibhav.musicx.utils.safeCall
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseMusicDataSource @Inject constructor(private val fireStore: FirebaseFirestore) :
    MusicDataSource {

    override suspend fun getAllMusic(): Resource<List<MusicDTO>> = safeCall {
        fireStore.collection(MUSIC_KEY).get().await().toObjects(MusicDTO::class.java)
    }

    override suspend fun uploadMusic(music: MusicDTO) = safeCall {
        fireStore.collection(MUSIC_KEY).document(music.title).set(music).await()
    }.mapToUnit()
}
