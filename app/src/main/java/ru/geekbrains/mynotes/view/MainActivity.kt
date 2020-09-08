package ru.geekbrains.mynotes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainActivity_toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = MainRecyclerViewAdapter()
        mainActivity_recycler.adapter = adapter

        viewModel.getViewState().observe(this,
            Observer{value ->
                value?.let{adapter.notesList = it}
            })
    }

}