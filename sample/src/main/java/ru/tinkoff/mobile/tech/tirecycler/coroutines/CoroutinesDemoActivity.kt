package ru.tinkoff.mobile.tech.tirecycler.coroutines

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.customAction
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick
import ru.tinkoff.mobile.tech.tirecycler.databinding.ActivityRecyclerBinding
import ru.tinkoff.mobile.tech.tirecycler.items.CustomActionUi
import ru.tinkoff.mobile.tech.tirecycler.items.HeaderUi
import ru.tinkoff.mobile.tech.tirecycler.items.MultiChoiceCheckUi
import ru.tinkoff.mobile.tech.tirecycler.items.TextUi
import ru.tinkoff.mobile.tech.tirecycler.items.TitleWithSubtitleUi
import ru.tinkoff.mobile.tech.tirecycler.items.getBaseRecyclerItems

class CoroutinesDemoActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val binding: ActivityRecyclerBinding by viewBinding(R.id.recyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val holderFactory = SampleCoroutinesTiRecyclerHolderFactory()
        val recycler = TiRecyclerCoroutines<ViewTyped>(binding.recyclerView, holderFactory) {
            layoutManager = LinearLayoutManager(this@CoroutinesDemoActivity)
            itemDismissCallbacks += ItemDismissTouchHelperCallback(
                this@CoroutinesDemoActivity,
                dismissibleViewType = R.layout.item_text,
                icon = ContextCompat.getDrawable(this@CoroutinesDemoActivity, R.drawable.ic_close_blue)!!,
                dismissBackgroundColor = Color.RED,
                normalBackgroundColor = Color.WHITE,
                elevation = 1f,
                cornerRadius = 2f,
                drawableOffsetInDp = 24,
            )
        }
        recycler.setItems(getBaseRecyclerItems())

        lifecycleScope.launch {
            listOf(
                recycler.clickedItem<TextUi>(R.layout.item_text)
                    .map { "TextUi: ${it.text}" },
                recycler.clickedItem<HeaderUi>(R.layout.item_header)
                    .map { "HeaderUi: ${it.text}" },
                recycler.clickedItem<TitleWithSubtitleUi>(R.layout.item_title_with_subtitle)
                    .map { "TitleWithSubtitleUi: ${it.title}\n ${it.subtitle}" },
                recycler.clickedItem<MultiChoiceCheckUi>(R.layout.item_multichoice)
                    .map { "MultiChoiceCheckUi: ${it.title}, isChecked: ${it.isChecked}" },
                recycler.swipeToDismiss<TextUi>(R.layout.item_text)
                    .map { "dismissed TextUi: ${it.text} dismissed" },
                recycler.customAction<CustomActionUi, CustomClick>(R.layout.item_custom_action)
                    .map { (_, click) -> click.title }
            )
                .merge()
                .collect {
                    Toast.makeText(this@CoroutinesDemoActivity, it, Toast.LENGTH_LONG).show()
                }
        }
    }
}
