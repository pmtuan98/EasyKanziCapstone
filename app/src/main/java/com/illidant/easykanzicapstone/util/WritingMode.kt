package com.illidant.easykanzicapstone.util

enum class WritingMode(val value: Int) {
    Hiragana(0),
    Vietnamese(1);

    companion object {
        fun fromInt(value: Int) = values().first { it.value == value }
    }
}
