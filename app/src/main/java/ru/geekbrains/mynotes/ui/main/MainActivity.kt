package ru.geekbrains.mynotes.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.model.Note
import ru.geekbrains.mynotes.ui.note.NoteActivity
import ru.geekbrains.mynotes.viewmodel.main.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivity_toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = MainRecyclerViewAdapter(object : MainRecyclerViewAdapter.OnItemClickListener{
            override fun onItemClick(note: Note) {
                openNoteScreen(note)
            }
        })

        mainActivity_recycler.adapter = adapter

        viewModel.getViewState().observe(this,
            Observer{value ->
                value?.let{adapter.notesList = it.notes}
            })

        mainActivity_fab.setOnClickListener { openNoteScreen(null) }
    }

    private fun openNoteScreen(note: Note?) {
        val intent = NoteActivity.getStartIntent(this, note)
        startActivity(intent)
    }

}