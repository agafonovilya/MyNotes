package ru.geekbrains.mynotes.ui.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.ui.base.BaseActivity
import ru.geekbrains.mynotes.ui.note.NoteActivity
import ru.geekbrains.mynotes.viewmodel.main.MainViewModel
import ru.geekbrains.mynotes.viewmodel.main.MainViewState

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    override val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override val layoutRes: Int = R.layout.activity_main
    private lateinit var adapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(mainActivity_toolbar)

        adapter = MainRecyclerViewAdapter(object : MainRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })

        mainActivity_recycler.adapter = adapter
        mainActivity_fab.setOnClickListener { openNoteScreen(null) }
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note?.id)
        startActivity(intent)
    }

    override fun renderData(data: List<Note>?) {
        if (data == null) return
        adapter.notesList = data
    }

}