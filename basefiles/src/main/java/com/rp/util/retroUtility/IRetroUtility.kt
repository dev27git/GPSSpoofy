package dev.rahul.baseutils.v1.util.retroUtility

import java.io.File

interface IRetroUtility {
    fun isConnected(): Boolean = false
    fun getCacheDirectory(): File? = null
}