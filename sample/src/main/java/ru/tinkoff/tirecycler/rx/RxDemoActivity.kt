package ru.tinkoff.tirecycler.rx

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler_rx2.TiRecyclerRx
import ru.tinkoff.tirecycler.R
import ru.tinkoff.tirecycler.databinding.ActivityRecyclerBinding
import ru.tinkoff.tirecycler.items.HeaderUi
import ru.tinkoff.tirecycler.items.MultiChoiceCheckUi
import ru.tinkoff.tirecycler.items.TextUi
import ru.tinkoff.tirecycler.items.TitleWithSubtitleUi
import ru.tinkoff.tirecycler.items.getBaseRecyclerItems


class RxDemoActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val binding: ActivityRecyclerBinding by viewBinding(R.id.recyclerView)

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val holderFactory = SampleTiRecyclerHolderFactory()
        val recycler = TiRecyclerRx<ViewTyped>(binding.recyclerView, holderFactory) {
            layoutManager = LinearLayoutManager(this@RxDemoActivity)
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
}
