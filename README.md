# TiRecycler

Build your RecyclerView screens fast with less duplication and provide more reusability. This
library was inspired by [AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates) but have
some important differences. More info in [Motivation](#motivation) section

# Quickstart:

## Dependencies

Add dependencies to your build.gradle

```kotlin
// pure base lib dependency without any concurrent work
implementation 'ru.tinkoff.mobile:ti-recycler:2.2.0'
// or 
// version with RxJava2 
implementation 'ru.tinkoff.mobile:ti-recycler-rx2:2.2.0'
// or
// coroutines(Flow) version
implementation 'ru.tinkoff.mobile:ti-recycler-coroutines:2.2.0'
```

# How to use it

Create your own instance of ViewTyped, let it be TextUi:

```kotlin
data class TextUi(
    val text: CharSequence,
    val background: Int? = null,
    val textSizeSp: Float = 16F,
    @ColorRes val textColorRes: Int = android.R.color.black,
    override val viewType: Int = R.layout.item_text,
    override val uid: String = text.toString()
) : ViewTyped
```

Then create instance of BaseRxViewHolder for this TextUi type:

```kotlin
open class TextUiViewHolder(view: View, clicks: TiRecyclerClickListener) :
    BaseViewHolder<TextUi>(view, clicks) {

    private val binding = ItemTextBinding.bind(view)

    override fun bind(item: TextUi) = with(binding) {
        title.text = item.text
        title.textSize = item.textSizeSp
        title.setTextColor(ContextCompat.getColor(title.context, item.textColorRes))
        item.background?.let(title::setBackgroundResource)
    }
}
```

Also we need to create instance of HolderFactory:

```kotlin
class SampleTiRecyclerHolderFactory : RxHolderFactory() {

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        return when (viewType) {
            R.layout.item_text -> TextUiViewHolder(view, clicks)
            else -> null
        }
    }
}
```

And now we need to bind it all together:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val recycler = TiRecycler(binding.recyclerView, SampleTiRecyclerHolderFactory()) {
        //here we could set parameters from TiRecyclerBuilder
        // for example:
        layoutManager = LinearLayoutManager(this@DemoActivity, RecyclerView.HORIZONTAL, false)
    }
    recycler.setItems(getBaseRecyclerItems())
}

private fun getBaseRecyclerItems(): List<ViewTyped> {
    return listOf(
        TextUi(text = "Here is simple example of TextUi"),
        HeaderUi(text = "Header 32sp", textSizeSp = 32f),
        TextUi(
            text = "Description - Lorem ipsum dolor sit amet",
            textColorRes = android.R.color.holo_purple
        ),
        TextUi(
            text = "Description - Lorem ipsum dolor sit amet, consectetur adipiscing elit",
            background = R.drawable.bg_rounded_grey
        )
    )
}
```

# How react on clicks

In your activity(or you may extract this logic to another file) you should add something like this:

Rx:

```kotlin

disposable = Observable.mergeArray(
    recycler.clickedItem<TextUi>(R.layout.item_text).map { "TextUi: ${it.text}" },
    recycler.clickedItem<HeaderUi>(R.layout.item_header).map { "HeaderUi: ${it.text}" },
).subscribe {
    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
}
```

Coroutines(Flow):

```kotlin

lifecycleScope.launch {
    listOf(
        recycler.clickedItem<TextUi>(R.layout.item_text)
            .map { "TextUi: ${it.text}" },
        recycler.clickedItem<HeaderUi>(R.layout.item_header)
            .map { "HeaderUi: ${it.text}" },
    )
        .merge()
        .collect { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
}
```

# How to use swipe to dismiss

First of all, you need to create a callback helper class

```kotlin
ItemDismissTouchHelperCallback(
    context,
    dismissibleViewType = youViewType,
    icon = yourDrawable,
    dismissBackgroundColor = Color.RED,
    normalBackgroundColor = Color.WHITE,
    elevation = 1f,
    cornerRadius = 2f,
    drawableOffsetInDp = 24,
)
```

and add it to `itemDismissCallbacks` in you builder function for TiRecycler (TiRecyclerCoroutines for coroutines version
or TiRecyclerRx for Rx version)

```kotlin
TiRecyclerCoroutines<ViewTyped>(recyclerView, holderFactory) {
    //...
    itemDismissCallbacks += ItemDismissTouchHelperCallback()
}
```

and after it, you can collect events via `recycler.swipeToDismiss` method

# How to use custom actions

You need:

* implement your own action with extends from `TiRecyclerCustomActionFlow/TiRecyclerCustomActionObservable`
* add it to map in your holder factory

```kotlin
class YourHolderFactory : HolderFactory() {

    private val yourCustomAction = YourCustomAction()

    override val customActions: Map<KClass<*>, RecyclerCustomActionObservable<*, *>>
        get() = mapOf(YourCustomAction::class to yourCustomAction)

    override fun createViewHolder(view: View, viewType: Int): BaseViewHolder<*>? {
        when (viewTyped) {
            R.layout.your_view_holder_layout -> YourCustomViewHolder(view, yourCustomAction)
        }
    }
}

class YourCustomViewHolder(
    view: View,
    customAction: YourCustomAction,
) : BaseViewHolder<YourUiModel>(view) {

    init {
        customAction.accept(this, view)
    }
}
```

and collect events via `recycler.customAction(viewType)`

More examples you can see
in [sample project](sample/src/main/java/ru/tinkoff/mobile/tech/tirecycler)

# Motivation

[RU: Habr](https://habr.com/ru/company/tinkoff/blog/665930/)

## License

TiRecycler is available under the Apache License 2.0. See the LICENSE file for more info.
