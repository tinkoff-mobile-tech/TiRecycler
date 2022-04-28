package ru.tinkoff.mobile.tech.ti_recycler.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

interface HolderFactory : (ViewGroup, Int) -> BaseViewHolder<ViewTyped> {

    fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>?

    @Suppress("UNCHECKED_CAST")
    override fun invoke(viewGroup: ViewGroup, viewType: Int): BaseViewHolder<ViewTyped> {
        val view: View = viewGroup.inflate(viewType)
        return checkNotNull(createViewHolder(view, viewType)) {
            "unknown viewType=" + viewGroup.resources.getResourceName(viewType)
        } as BaseViewHolder<ViewTyped>
    }

    private fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(resource, this, attachToRoot)
    }
}
