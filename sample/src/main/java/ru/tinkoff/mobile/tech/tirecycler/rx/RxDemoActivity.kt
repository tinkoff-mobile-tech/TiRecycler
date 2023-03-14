package ru.tinkoff.mobile.tech.tirecycler.rx

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.ti_recycler.swipes.ItemDismissTouchHelperCallback
import ru.tinkoff.mobile.tech.ti_recycler_rx2.TiRecyclerRx
import ru.tinkoff.mobile.tech.ti_recycler_rx2.customAction
import ru.tinkoff.mobile.tech.tirecycler.R
import ru.tinkoff.mobile.tech.tirecycler.actions.CustomClick
import ru.tinkoff.mobile.tech.tirecycler.databinding.ActivityRecyclerBinding
import ru.tinkoff.mobile.tech.tirecycler.items.CustomActionUi
import ru.tinkoff.mobile.tech.tirecycler.items.HeaderUi
import ru.tinkoff.mobile.tech.tirecycler.items.MultiChoiceCheckUi
import ru.tinkoff.mobile.tech.tirecycler.items.TextUi
import ru.tinkoff.mobile.tech.tirecycler.items.TitleWithSubtitleUi
import ru.tinkoff.mobile.tech.tirecycler.items.getBaseRecyclerItems


class RxDemoActivity : AppCompatActivity(R.layout.activity_recycler) {

    private val binding: ActivityRecyclerBinding by viewBinding(R.id.recyclerView)

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val holderFactory = SampleTiRecyclerHolderFactory()
        val recycler = TiRecyclerRx<ViewTyped>(binding.recyclerView, holderFactory) {
            layoutManager = LinearLayoutManager(this@RxDemoActivity)
            itemDismissCallbacks += ItemDismissTouchHelperCallback(
                context = this@RxDemoActivity,
                dismissibleViewType = R.layout.item_text,
                icon = ContextCompat.getDrawable(this@RxDemoActivity, R.drawable.ic_close_blue)!!,
                dismissBackgroundColor = Color.RED,
                normalBackgroundColor = Color.WHITE,
                elevation = 1f,
                cornerRadius = 2f,
                drawableOffsetInDp = 56,
            )
        }
        recycler.setItems(getBaseRecyclerItems())

        disposable = Observable.mergeArray(
            recycler.clickedItem<TextUi>(R.layout.item_text).map { "TextUi: ${it.text}" },
            recycler.clickedItem<HeaderUi>(R.layout.item_header).map { "HeaderUi: ${it.text}" },
            recycler.clickedItem<TitleWithSubtitleUi>(R.layout.item_title_with_subtitle)
                .map { "TitleWithSubtitleUi: ${it.title}\n ${it.subtitle}" },
            recycler.clickedItem<MultiChoiceCheckUi>(R.layout.item_multichoice)
                .map { "MultiChoiceCheckUi: ${it.title}, isChecked: ${it.isChecked}" },
            recycler.swipeToDismiss<TextUi>(R.layout.item_text).map { "dismissed TextUi: ${it.text} dismissed" },
            recycler.customAction<CustomActionUi, CustomClick>(R.layout.item_custom_action)
                .map { (_, click) -> click.title }
        ).subscribe {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) disposable.dispose()
    }
}
