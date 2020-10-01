package ru.geekbrains.mynotes.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ReceiveChannel
import ru.geekbrains.mynotes.data.errors.NoAuthException
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val firebaseAuth: FirebaseAuth,
                        private val db: FirebaseFirestore) : RemoteDataProvider {
    private val TAG = "${FireStoreProvider::class.java.simpleName} :"

    private val currentUser
        get() = firebaseAuth.currentUser

    private val notesReference
        get() = currentUser ?.let {
            db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()



    override fun subscribeToAllNotes(): ReceiveChannel<Result> = Channel<Result>(CONFLATED).apply {
        var registration: ListenerRegistration? = null
            try {
                registration = notesReference.addSnapshotListener{ snapshot, e ->
                    val value = e?.let {
                        Result.Error(it)
                    } ?: snapshot?.let {
                        val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                        Result.Success(notes)
                    }
                    value?.let {
                        offer(it)
                    }
                }
            }catch (t: Throwable) {
                offer(Result.Error(t))
            }
        invokeOnClose {
            registration?.remove()
        }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        continuation.resume(
            currentUser?.let {
                User(it.displayName ?: "", it.email ?: "")
            }
        )
    }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
            try {
                notesReference.document(note.id).set(note)
                    .addOnSuccessListener {
                        continuation.resume(note)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (t: Throwable) {
                continuation.resumeWithException(t)
            }
        }

    override suspend fun getNoteById(id: String): Note? = suspendCoroutine { continuation ->
            try {
                notesReference.document(id).get()
                    .addOnSuccessListener {
                        continuation.resume(it.toObject(Note::class.java))
                    }.addOnFailureListener {
                        throw it
                    }
            } catch (t: Throwable) {
                continuation.resumeWithException(t)
            }
        }


    override suspend fun deleteNote(noteId: String): Unit = suspendCoroutine { continuation ->
            try {
                notesReference.document(noteId).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }.addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (t: Throwable) {
                continuation.resumeWithException(t)
            }
        }
}