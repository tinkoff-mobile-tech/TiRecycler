package ru.tinkoff.mobile.tech.ti_recycler_coroutines.clicks

import android.widget.CompoundButton

data class CheckItemChange(val viewType: Int, val compoundView: CompoundButton, val position: Int)