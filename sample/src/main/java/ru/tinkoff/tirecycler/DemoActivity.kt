package ru.tinkoff.tirecycler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.TiRecyclerRx
import ru.tinkoff.tirecycler.databinding.ActivityRecyclerBinding
import ru.tinkoff.tirecycler.items.HeaderUi
import ru.tinkoff.tirecycler.items.MultiChoiceCheckUi
import ru.tinkoff.tirecycler.items.TextUi
import ru.tinkoff.tirecycler.items.TitleWithSubtitleUi


class DemoActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val binding: ActivityRecyclerBinding by viewBinding(R.id.recyclerView)

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val holderFactory = SampleTiRecyclerHolderFactory()
        val recycler = TiRecyclerRx<ViewTyped>(binding.recyclerView, holderFactory) {
            layoutManager = LinearLayoutManager(this@DemoActivity)
        }
        recycler.setItems(getBaseRecyclerItems())

        disposable = Observable.mergeArray(
            recycler.clickedItem<TextUi>(R.layout.item_text).map { "TextUi: ${it.text}" },
            recycler.clickedItem<HeaderUi>(R.layout.item_header).map { "HeaderUi: ${it.text}" },
            recycler.clickedItem<TitleWithSubtitleUi>(R.layout.item_title_with_subtitle)
                .map { "TitleWithSubtitleUi: ${it.title}\n ${it.subtitle}" },
            recycler.clickedItem<MultiChoiceCheckUi>(R.layout.item_multichoice)
                .map { "MultiChoiceCheckUi: ${it.title}" },
        ).subscribe {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) disposable.dispose()
    }

    private fun getBaseRecyclerItems(): List<ViewTyped> {
        return listOf(
            TextUi(text = "Below you can see examples of usage sample items"),
            TextUi(text = "In our project these items are basic and we put them in base HolderFactory"),
            HeaderUi(text = "Header 32sp", textSizeSp = 32f),
            TextUi(text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit"),
            TextUi(
                text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                background = R.drawable.bg_rounded_grey
            ),
            TextUi(
                text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                leftIcon = R.drawable.ic_close_blue
            ),
            TextUi(
                text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                leftIcon = R.drawable.ic_close_blue,
                background = R.drawable.bg_rounded_grey
            ),
            HeaderUi(text = "Header 24sp", textSizeSp = 24f),
            TextUi(text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit"),
            HeaderUi(text = "Header 16sp"),
            TextUi(text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit"),
            TitleWithSubtitleUi(
                "Some title",
                "Some subtitle - Lorem ipsum dolor sit amet, consectetur adipiscing elit"
            ),
            TitleWithSubtitleUi(
                "Title + icon",
                "Description - single line only",
                icon = R.drawable.ic_close_blue
            ),
            MultiChoiceCheckUi("Checkbox", false),
        )
    }
}
