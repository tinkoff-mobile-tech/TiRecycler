# TiRecycler

Build your RecyclerView screens fast with less duplication and provide more reusability. This
library was inspired by [AdapterDelegates](https://github.com/sockeqwe/AdapterDelegates) but have
some important differences. More info in [Motivation](#motivation) section

# Quickstart:

## Dependencies

Add dependencies to your build.gradle

```kotlin
//pure base lib dependency without any concurrent work (but you will need to implement some basic stuff to work with click and etc.)
implementation 'ru.tinkoff:ti-recycler:1.0.0'
//or 
// version with RxJava2 
implementation 'ru.tinkoff:ti-recycler-rx2:1.0.0'

//if you want coroutines version feel free to contribute or wait for me doing it
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
open class TextUiViewHolder(view: View, clicks: TiRecyclerHolderClickListener) :
    BaseRxViewHolder<TextUi>(view, clicks) {

    private val binding = ItemTextBinding.bind(view.findViewById(R.id.title))

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

    override fun createViewHolder(view: View, viewType: Int): BaseRxViewHolder<*>? {
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

```kotlin

disposable = Observable.mergeArray(
        recycler.clickedItem<TextUi>(R.layout.item_text).map { "TextUi: ${it.text}" },
        recycler.clickedItem<HeaderUi>(R.layout.item_header).map { "HeaderUi: ${it.text}" },
    ).subscribe {
        Toast.makeText(this, it, Toast.LENGTH_LONG).show()
    }
```

More examples you can see in [sample project](sample/src/main/java/ru/tinkoff/tirecycler)

# Motivation

[RU: Habr](https://habr.com/ru/company/tinkoff/blog/665930/)

Eng translation will be added soon

## License

TiRecycler is available under the Apache License 2.0. See the LICENSE file for more info.
