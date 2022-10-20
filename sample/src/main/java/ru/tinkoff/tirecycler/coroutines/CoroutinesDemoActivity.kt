package ru.tinkoff.tirecycler.coroutines

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_coroutines.TiRecyclerCoroutines
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.databinding.ActivityRecyclerBinding
import ru.tinkoff.tirecycler.items.HeaderUi
import ru.tinkoff.tirecycler.items.MultiChoiceCheckUi
import ru.tinkoff.tirecycler.items.TextUi
import ru.tinkoff.tirecycler.items.TitleWithSubtitleUi
import ru.tinkoff.tirecycler.items.getBaseRecyclerItems

class CoroutinesDemoActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val binding: ActivityRecyclerBinding by viewBinding(R.id.recyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val holderFactory = SampleCoroutinesTiRecyclerHolderFactory()
        val recycler = TiRecyclerCoroutines<ViewTyped>(binding.recyclerView, holderFactory) {
            layoutManager = LinearLayoutManager(this@CoroutinesDemoActivity)
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
            )
                .merge()
                .collect { Toast.makeText(this@CoroutinesDemoActivity, it, Toast.LENGTH_LONG).show() }
        }
    }
}
