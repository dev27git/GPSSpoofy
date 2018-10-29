package com.rp.basefiles

import android.os.Bundle
import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


interface IBaseView {

    val bundleData: Bundle
    val isConnectedToNetwork: Boolean

    fun enableBackButton()
    fun disableBackButton()

    fun setTitle(title: String)
    fun setSubTitle(subTitle: String)

    fun onAttachSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout)

    fun onShowLoading()
    fun onHideLoading()

    fun initializeSnackBar(view: View)
    fun showSnackBarMessage(message: String)

    fun hideKeyboard(view: View)
}