package ru.tinkoff.mobile.tech.tirecycler.items

import android.view.View
import androidx.annotation.DrawableRes
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.databinding.ItemMultichoiceBinding
import ru.tinkoff.mobile.tech.tirecycler.util.setCompoundDrawables

data class MultiChoiceCheckUi(
    val title: CharSequence,
    val isChecked: Boolean,
    @DrawableRes val icon: Int? = null,
    override val viewType: Int = R.layout.item_multichoice,
    override val uid: String = title.toString()
) : ViewTyped

class MultiChoiceCheckViewHolder(
    view: View,
    clicks: TiRecyclerClickListener
) : BaseViewHolder<MultiChoiceCheckUi>(view) {

    private val binding = ItemMultichoiceBinding.bind(view)

    init {
        clicks.accept(this) { binding.title.isChecked = !binding.title.isChecked }
    }

    override fun bind(item: MultiChoiceCheckUi) {
        binding.title.text = item.title
        binding.title.isChecked = item.isChecked
        item.icon?.let { binding.title.setCompoundDrawables(it) }
    }
}
