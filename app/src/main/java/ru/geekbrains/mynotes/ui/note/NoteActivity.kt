package ru.geekbrains.mynotes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_note.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.extensions.DATE_TIME_FORMAT
import ru.geekbrains.mynotes.model.Color
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.ui.base.BaseActivity
import ru.geekbrains.mynotes.viewmodel.note.NoteViewModel
import ru.geekbrains.mynotes.viewmodel.note.NoteViewState
import java.text.SimpleDateFormat
import java.util.*

private const val SAVE_DELAY = 2000L

@Suppress("DEPRECATION")
class NoteActivity : BaseActivity<Note?, NoteViewState>() {

     companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"

        fun getStartIntent(context: Context, noteId: String?): Intent {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            return intent
        }
    }

    override val viewModel: NoteViewModel by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val noteId = intent.getStringExtra(EXTRA_NOTE)

        setSupportActionBar(noteActivity_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        noteId?.let {
            viewModel.loadNote(it)
        }

        if (noteId == null ) supportActionBar?.title = getString(R.string.new_note_title)

        noteActivity_titleEt.doAfterTextChanged { triggerSaveNote() }
        noteActivity_bodyEt.doAfterTextChanged { triggerSaveNote() }
    }

    override fun renderData(data: Note?) {
        this.note = data
        initView()
    }

    private fun initView() {
        if (note != null) {
            noteActivity_titleEt.setText(note?.title ?: "")
            noteActivity_bodyEt.setText(note?.note ?: "")
            val color = when(note!!.color) {
                Color.WHITE -> R.color.color_white
                Color.VIOLET -> R.color.color_violet
                Color.YELLOW -> R.color.color_yello
                Color.RED -> R.color.color_red
                Color.PINK -> R.color.color_pink
                Color.GREEN -> R.color.color_green
                Color.BLUE -> R.color.color_blue
            }

            noteActivity_toolbar.setBackgroundColor(resources.getColor(color))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun triggerSaveNote() {
        if (noteActivity_titleEt.text?.length?:0 < 3) return

        Handler().postDelayed({
            note = note?.copy(title = noteActivity_titleEt.text.toString(),
                note = noteActivity_bodyEt.text.toString(),
                lastChanged = Date())
                ?: createNewNote()

            if (note != null) viewModel.saveChanges(note!!)
        }, SAVE_DELAY)
    }

    private fun createNewNote(): Note = Note(UUID.randomUUID().toString(),
        noteActivity_titleEt.text.toString(),
        noteActivity_bodyEt.text.toString())
}
