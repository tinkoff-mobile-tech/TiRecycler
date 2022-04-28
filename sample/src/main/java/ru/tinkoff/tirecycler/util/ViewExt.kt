package ru.tinkoff.tirecycler.util

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isGone

fun TextView.removeAllCompoundDrawables() =
    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)

fun TextView.setCompoundDrawables(
    @DrawableRes left: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes bottom: Int = 0
) = setCompoundDrawablesWithIntrinsicBounds(
    left,
    top,
    right,
    bottom
)

fun TextView.setTextIfNotNullOrGone(text: CharSequence?) {
    isGone = text == null
    text?.let(::setText)
}

fun ImageView.setImageResourceOrGone(@DrawableRes res: Int?) {
    isGone = res == null
    res?.let(::setImageResource)
}
