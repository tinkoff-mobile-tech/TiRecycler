package ru.tinkoff.mobile.tech.ti_recycler_coroutines.util

import android.os.Looper

internal fun checkMainThread() {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
}