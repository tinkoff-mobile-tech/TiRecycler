package ru.tinkoff.mobile.tech.tirecycler.items

import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped
import ru.tinkoff.mobile.tech.tirecycler.R

fun getBaseRecyclerItems(): List<ViewTyped> {
    return listOf(
        CustomActionUi(),
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
