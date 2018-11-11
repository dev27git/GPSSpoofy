package com.dev.rahul.imago.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.rp.basefiles.BaseActivity
import com.rp.basefiles.IBaseView


abstract class BaseFragment : Fragment(), IBaseView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var baseActivity: BaseActivity

    abstract val layoutRes: Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivity
    }

    override fun onAttachSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun enableBackButton() = baseActivity.enableBackButton()

    override fun disableBackButton() = baseActivity.disableBackButton()

    override fun setTitle(title: String) = baseActivity.setTitle(title)

    override fun setSubTitle(subTitle: String) = baseActivity.setSubTitle(subTitle)

    override fun onShowLoading() {
            swipeRefreshLayout.isRefreshing = true
    }

    override fun onHideLoading() {
            swipeRefreshLayout.isRefreshing = false
    }

    override fun initializeSnackBar(view: View) = baseActivity.initializeSnackBar(view)

    override fun showSnackBarMessage(message: String) = baseActivity.showSnackBarMessage(message)

    override fun hideKeyboard(view: View) = baseActivity.hideKeyboard(view)

    override val bundleData: Bundle
        get() = baseActivity.bundleData

    override val isConnectedToNetwork: Boolean
        get() = baseActivity.isConnectedToNetwork
}