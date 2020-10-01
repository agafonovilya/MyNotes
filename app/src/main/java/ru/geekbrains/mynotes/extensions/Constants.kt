package ru.geekbrains.mynotes.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.model.Note
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String =
    SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        .format(this)

fun Note.Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(context, getColorRes())

fun Note.Color.getColorRes(): Int = when (this) {
    Note.Color.WHITE -> R.color.color_white
    Note.Color.VIOLET -> R.color.color_violet
    Note.Color.YELLOW -> R.color.color_yello
    Note.Color.RED -> R.color.color_red
    Note.Color.PINK -> R.color.color_pink
    Note.Color.GREEN -> R.color.color_green
    Note.Color.BLUE -> R.color.color_blue
}