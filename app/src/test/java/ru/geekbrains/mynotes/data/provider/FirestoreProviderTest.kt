package ru.geekbrains.mynotes.data.provider

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.geekbrains.mynotes.data.errors.NoAuthException
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.model.Result

class FirestoreProviderTest {

    @get:Rule
    val taskExecutionRule = InstantTaskExecutorRule()

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockResultCollection = mockk<CollectionReference>()

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()

    private val testNotes = listOf(Note("1"), Note("2"), Note("3"))

    @Before
    fun setup() {
        clearAllMocks()
        every { mockUser.uid } returns ""
        every { mockAuth.currentUser } returns mockUser
        every { mockDb.collection(any()).document(any()).collection(any()) } returns mockResultCollection

        every { mockDocument1.toObject(Note::class.java) } returns testNotes[0]
        every { mockDocument2.toObject(Note::class.java) } returns testNotes[1]
        every { mockDocument3.toObject(Note::class.java) } returns testNotes[2]
    }

    @Test
    fun `should throw NoAuthException if not auth`() =  runBlocking {
        every { mockAuth.currentUser } returns null
        val result = (provider.subscribeToAllNotes().receive() as? Result.Error)?.error
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `saveNote calls set`() =  runBlocking {
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        val slot = slot<OnSuccessListener<in Void>>()
        val mockTask = mockk<Task<Void>>()
        every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } returns mockTask
        every { mockDocumentReference.set(testNotes[0]) } returns mockTask

        launch {
            provider.saveNote(testNotes[0])
        }
        delay(1)
        slot.captured.onSuccess(null)
        verify(exactly = 1) { mockDocumentReference.set(testNotes[0]) }
    }

    @Test
    fun `saveNote return success note`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()

        val mockTask = mockk<Task<Void>>()
        every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } returns mockTask
        every { mockDocumentReference.set(testNotes[0]) } returns mockTask
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        val deferred = async {
            provider.saveNote(testNotes[0])
        }
        delay(1)

        slot.captured.onSuccess(null)

        assertEquals(deferred.await(), testNotes[0])
    }

    @Test
    fun `subscribeToAllNotes returns notes`() = runBlocking {
        val mockSnapshot = mockk<QuerySnapshot>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockSnapshot.documents } returns listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val deferred = async {
            (provider.subscribeToAllNotes().receive() as? Result.Success<*>)?.data
        }
        delay(1)

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(deferred.await(), testNotes)
    }

    @Test
    fun `subscribeToAllNotes returns error`()  = runBlocking {
        val testError = mockk<FirebaseFirestoreException>()
        val slot = slot<EventListener<QuerySnapshot>>()

        every { mockResultCollection.addSnapshotListener(capture(slot)) } returns mockk()

        val deferred = async {
            (provider.subscribeToAllNotes().receive() as? Result.Error)?.error
        }
        delay(1)

        slot.captured.onEvent(null, testError)
        assertEquals(deferred.await(), testError)
    }

    @Test
    fun `deleteNote calls delete`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>(relaxed = true)
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        val slot = slot<OnSuccessListener<in Void>>()
        val mockTask = mockk<Task<Void>>()
        every { mockTask.addOnSuccessListener(capture(slot)) } returns mockTask
        every { mockTask.addOnFailureListener(any()) } returns mockTask
        every { mockDocumentReference.delete() } returns mockTask

        launch {
            provider.deleteNote(testNotes[0].id)
        }
        delay(1)

        slot.captured.onSuccess(null)
        verify(exactly = 1) { mockDocumentReference.delete() }
    }

    @Test
    fun `deleteNote returns Unit`() = runBlocking {
        val mockDocumentReference = mockk<DocumentReference>()
        val slot = slot<OnSuccessListener<Void>>()

        val mockTask = mockk<Task<Void>>()
        every { mockTask
            .addOnSuccessListener(capture(slot))
            .addOnFailureListener(any())} returns mockTask

        every { mockDocumentReference.delete() } returns mockTask
        every { mockResultCollection.document(testNotes[0].id) } returns mockDocumentReference

        val deferred = async {
            provider.deleteNote(testNotes[0].id)
        }
        delay(1)

        slot.captured.onSuccess(null)

        assertEquals(deferred.await(), Unit)
    }

    @Test
    fun `getNoteByID returns note`() = runBlocking {
        val slot = slot<OnSuccessListener<in DocumentSnapshot>>()
        every {
            mockResultCollection
                .document(any())
                .get()
                .addOnSuccessListener(capture(slot))
                .addOnFailureListener(capture(slot()))} returns mockk()

        val deferred = async {
            provider.getNoteById(testNotes[0].id)
        }

        delay(1)
        slot.captured.onSuccess(mockDocument1)
        assertEquals(deferred.await(), testNotes[0])
    }
}
