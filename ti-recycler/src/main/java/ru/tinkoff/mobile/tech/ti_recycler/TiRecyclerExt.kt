@file:Suppress("UNCHECKED_CAST")

package ru.tinkoff.mobile.tech.ti_recycler

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.adapters.AsyncTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.adapters.SimpleTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

fun <VT : ViewTyped, HF: HolderFactory> produceAdapter(
    holderFactory: HF,
    diffCallback: DiffUtil.ItemCallback<VT>? = null
): BaseTiAdapter<VT, HF> = diffCallback?.run {
    AsyncTiAdapter(holderFactory, this)
} ?: SimpleTiAdapter(holderFactory)

val RecyclerView.requireAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    get() = requireNotNull(adapter)

val RecyclerView.baseAdapter: BaseTiAdapter<ViewTyped, HolderFactory>
    get() = adapter as BaseTiAdapter<ViewTyped, HolderFactory>

internal fun View.dpToPx(sizeInDp: Int): Int = dpToPx(resources.displayMetrics, sizeInDp.toFloat())

internal fun Context.dpToPx(sizeInDp: Int): Int = dpToPx(resources.displayMetrics, sizeInDp.toFloat())

private fun dpToPx(displayMetrics: DisplayMetrics, sizeInDp: Float): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sizeInDp, displayMetrics).toInt()
}
