package ru.geekbrains.mynotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.model.Color
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

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) {
            with(itemView) {
                val color = ContextCompat.getColor(context,
                    when (note.color) {
                        Color.WHITE -> R.color.color_white
                        Color.VIOLET -> R.color.color_violet
                        Color.YELLOW -> R.color.color_yello
                        Color.RED -> R.color.color_red
                        Color.PINK -> R.color.color_pink
                        Color.GREEN -> R.color.color_green
                        Color.BLUE -> R.color.color_blue
                    }
                )

                card_title.text = note.title
                card_body.text = note.note
                card_container.setBackgroundColor(color)
                setOnClickListener{onItemClickListener(note)}
            }
        }

    }

}




