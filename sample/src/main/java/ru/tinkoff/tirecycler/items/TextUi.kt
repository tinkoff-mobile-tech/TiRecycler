package ru.tinkoff.tirecycler.items

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import ru.tinkoff.mobile.tech.ti_recycler.base.BaseViewHolder
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.clicks.TiRecyclerClickListener
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.databinding.ItemTextBinding
import ru.tinkoff.tirecycler.util.removeAllCompoundDrawables
import ru.tinkoff.tirecycler.util.setCompoundDrawables

data class TextUi(
    val text: CharSequence,
    @DrawableRes val leftIcon: Int? = null,
    val background: Int? = null,
    val textSizeSp: Float = 16F,
    @ColorRes val textColorRes: Int = android.R.color.holo_purple,
    override val viewType: Int = R.layout.item_text,
    override val uid: String = text.toString()
) : ViewTyped

open class TextUiViewHolder(
    view: View,
    clicks: TiRecyclerClickListener
) : BaseViewHolder<TextUi>(view, clicks) {

    private val binding = ItemTextBinding.bind(view)

    override fun bind(item: TextUi) = with(binding) {
        title.text = item.text
        title.textSize = item.textSizeSp
        item.background?.let(title::setBackgroundResource)
        title.setTextColor(ContextCompat.getColor(title.context, item.textColorRes))
        if (item.leftIcon == null) {
            title.removeAllCompoundDrawables()
        } else {
            title.setCompoundDrawables(item.leftIcon)
        }
    }
}
