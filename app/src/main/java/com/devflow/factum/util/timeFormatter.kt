package com.devflow.factum.util

fun timeFormatter(time: Int): String {
    return if (time.toString().length == 1) {
        "0$time"
    } else {
        time.toString()
    }
}