package com.sun.basic_japanese.data.source

interface OnDataLoadedCallback<T> {
    fun onSuccess(data: T)
    fun onFailed(exception: Exception)
}
