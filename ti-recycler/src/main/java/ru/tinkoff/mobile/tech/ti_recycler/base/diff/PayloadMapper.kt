package ru.tinkoff.mobile.tech.ti_recycler.base.diff

import ru.tinkoff.mobile.tech.ti_recycler.base.ViewTyped

fun interface PayloadMapper<T : ViewTyped> : (T) -> Any?
