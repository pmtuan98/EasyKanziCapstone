package com.illidant.easykanzicapstone.domain.model

abstract class Kanji {
    abstract val id: Int
    abstract val word: String
    abstract val vietMean: String
    abstract val chinaMean: String
    abstract val onjomi: String
    abstract val romajiOnjomi: String
    abstract val kunjomi: String
    abstract val romajiKunjomi: String
    abstract val strokeCount: Int
    abstract val example: String
    abstract var favorite: String
    abstract var tag: String
}