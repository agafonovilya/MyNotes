package ru.geekbrains.mynotes.data.provider

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import ru.geekbrains.mynotes.data.errors.NoAuthException
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result
import ru.geekbrains.mynotes.model.User

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



    override fun subscribeToAllNotes(): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                notesReference.addSnapshotListener { snapshot, e ->
                    e?.let {
                        value = Result.Error(it)
                    } ?: snapshot?.let {
                        val notes = snapshot.documents.mapNotNull { it.toObject(Note::class.java) }
                        value = Result.Success(notes)
                    }
                }
            }catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                notesReference.document(note.id)
                    .set(note).addOnSuccessListener {
                        Log.d(TAG, "Note $note is saved")
                        value = Result.Success(note)
                    }.addOnFailureListener {
                        Log.d(TAG, "Error saving note $note, message: ${it.message}")
                        throw it
                    }
            } catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun getNoteById(id: String): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                notesReference.document(id).get()
                    .addOnSuccessListener {
                        value = Result.Success(it.toObject(Note::class.java))
                    }.addOnFailureListener {
                        throw it
                    }
            } catch (e: Throwable) {
                value = Result.Error(e)
            }
        }

    override fun getCurrentUser(): LiveData<User?> =
        MutableLiveData<User?>().apply {
            value = currentUser?.let { User(it.displayName ?: "",
                it.email ?: "") }
        }

    override fun deleteNote(noteId: String): LiveData<Result> =
        MutableLiveData<Result>().apply {
            try {
                notesReference.document(noteId).delete()
                    .addOnSuccessListener {
                        value = Result.Success(noteId)
                    }.addOnFailureListener {
                        value = Result.Error(it)
                    }
            } catch (t: Throwable) {
                value = Result.Error(t)
            }
        }
}