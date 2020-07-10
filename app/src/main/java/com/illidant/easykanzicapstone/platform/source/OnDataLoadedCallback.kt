package com.illidant.easykanzicapstone.platform.source

interface OnDataLoadedCallback<T> {
    fun onSuccess(data: T)
    fun onFailed(exception: Exception)
}
