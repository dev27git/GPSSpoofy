package dev.rahul.baseutils.v1.util.imageUtility

interface OnImageLoaderCallback {
    fun onStart()
    fun onComplete()
    fun onError(e: Exception?)
}