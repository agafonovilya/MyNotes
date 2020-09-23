package ru.geekbrains.mynotes.ui.note

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_note.*
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.extensions.format
import ru.geekbrains.mynotes.extensions.getColorInt
import ru.geekbrains.mynotes.model.Color
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.ui.base.BaseActivity
import ru.geekbrains.mynotes.viewmodel.note.NoteViewModel
import ru.geekbrains.mynotes.viewmodel.note.NoteViewState
import java.util.*

private const val SAVE_DELAY = 2000L

class DeleteDialog : DialogFragment() {
    companion object {
        val TAG = DeleteDialog::class.java.name + "TAG"
        fun createInstance() = DeleteDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context!!)
            .setTitle(R.string.delete_dialog_title)
            .setMessage(R.string.delete_dialog_message)
            .setPositiveButton(R.string.ok_bth_title) { _, _ -> (activity as DeleteListener).onDelete() }
            .setNegativeButton(R.string.logout_dialog_cancel) { _, _ -> dismiss() }
            .create()

    interface DeleteListener {
        fun onDelete()
    }
}

@Suppress("DEPRECATION")
class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

     companion object {
         private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
         private val TAG_DELETE = "DeleteDialog TAG"

         fun getStartIntent(context: Context, noteId: String?): Intent {
             val intent = Intent(context, NoteActivity::class.java)
             intent.putExtra(EXTRA_NOTE, noteId)
             return intent
         }
    }

    override val viewModel: NoteViewModel by viewModel()

    override val layoutRes: Int = R.layout.activity_note
    private var note: Note? = null
    private var textWatcher: TextWatcher? = null
    private var color: Color = Color.YELLOW

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

        colorPicker.onColorClickListener = {
            color = it
            setToolbarColor(it)
            triggerSaveNote()
        }

        viewModel.showProgress().observe(this, object : Observer<Boolean> {
            override fun onChanged(value:Boolean){
                progress_bar.visibility = if (value) View.VISIBLE else View.GONE
            }
        })
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        if(data.note?.color == note?.color &&
            data.note?.title == note?.title &&
            data.note?.note == note?.note) return
        this.note = data.note
        data.note?.let { color = it.color }
        initView()
    }

    private fun initView() {
        note?.run {
            supportActionBar?.title = lastChanged.format()
            noteActivity_toolbar.setBackgroundColor(
                color.getColorInt(this@NoteActivity)
            )

            removeEditListener()
            noteActivity_titleEt.setText(title)
            noteActivity_bodyEt.setText(note)
            setEditListener()
        }
    }

    private fun setEditListener() {
        textWatcher = noteActivity_titleEt.doAfterTextChanged { triggerSaveNote() }
        noteActivity_bodyEt.addTextChangedListener(textWatcher);
    }

    private fun removeEditListener() {
        noteActivity_titleEt.removeTextChangedListener(textWatcher)
        noteActivity_bodyEt.removeTextChangedListener(textWatcher)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.palette -> togglePalette().let{true}
        R.id.delete -> deleteNote().let { true }
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean =
        menuInflater.inflate(R.menu.note_menu, menu).let { true }

    private fun deleteNote() {
        supportFragmentManager.findFragmentByTag(DeleteDialog.TAG) ?:
        DeleteDialog.createInstance().show(supportFragmentManager, DeleteDialog.TAG)
    }

    override fun onDelete() {
        viewModel.deleteNote()
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    override fun onBackPressed() {
        if (colorPicker.isOpen) {
            colorPicker.close()
            return
        }
        super.onBackPressed()
    }

    private fun setToolbarColor(color: Color) {
        noteActivity_toolbar.setBackgroundColor(color.getColorInt(this))
    }
}
