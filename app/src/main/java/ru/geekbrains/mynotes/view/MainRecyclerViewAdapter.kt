package ru.geekbrains.mynotes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.viewmodel.Note

class MainRecyclerViewAdapter : RecyclerView.Adapter<MainRecyclerViewAdapter.RecyclerViewHolder>() {

    var notesList: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
        )

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) =
        holder.bind(notesList[position])

    override fun getItemCount() = notesList.size

    class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) =
            with(itemView) {
                card_title.text = note.title
                card_body.text = note.note
                card_container.setBackgroundColor(note.color)
            }
    }

}