package ru.tinkoff.mobile.tech.ti_recycler

import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.mobile.tech.ti_recycler.adapters.BaseTiAdapter
import ru.tinkoff.mobile.tech.ti_recycler.base.HolderFactory
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

@Suppress("VarCouldBeVal")
interface BaseTiRecycler<T : ViewTyped, HF : HolderFactory> {

    val adapter: BaseTiAdapter<T, HF>

    fun setItems(items: List<T>)
}
