package ru.geekbrains.mynotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.extensions.getColorInt
import ru.geekbrains.mynotes.model.Note

class MainRecyclerViewAdapter(private val onItemClickListener: (Note) -> Unit):
    RecyclerView.Adapter<MainRecyclerViewAdapter.RecyclerViewHolder>() {

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

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int): Unit {
        holder.bind(notesList[position])
    }

    override fun getItemCount() = notesList.size

    inner class RecyclerViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            card_title.text = note.title
            card_body.text = note.note

            itemView.setBackgroundColor(note.color.getColorInt(itemView.context))
            itemView.setOnClickListener { onItemClickListener(note) }

        }
    }

}




