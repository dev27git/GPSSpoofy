package com.rp.basefiles

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.rp.util.fragment.FragmentBuilder
import com.rp.util.toast.RToast


abstract class BaseActivity : AppCompatActivity(), IBaseView, SwipeRefreshLayout.OnRefreshListener {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private lateinit var context: Context

    private var snackBar: Snackbar? = null

    abstract val layoutRes: Int

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        context = this
        FragmentBuilder.initManager(supportFragmentManager)
        RToast.init(this);
    }

    override fun onAttachSwipeRefreshLayout(swipeRefreshLayout: SwipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun enableBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun disableBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun setTitle(titleId: Int) {
        supportActionBar?.setTitle(titleId)
    }

    override fun setSubTitle(subTitle: String) {
        supportActionBar?.subtitle = subTitle
    }

    override fun onShowLoading() {
            swipeRefreshLayout?.isRefreshing = true
    }

    override fun onHideLoading() {
            swipeRefreshLayout?.isRefreshing = false
    }

    override fun initializeSnackBar(view: View) {
        snackBar = Snackbar.make(view, "Please try again", Snackbar.LENGTH_LONG)
    }

    override fun showSnackBarMessage(message: String) {
        snackBar?.let {
            it.setText(message)
            it.setAction("Dismiss") {}
            it.show()
        }
    }

    override fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override val bundleData: Bundle
        get() = intent?.extras ?: Bundle.EMPTY


    override val isConnectedToNetwork: Boolean
        get() {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork?.isConnected ?: false
        }

    fun Toolbar.setFont(font: Typeface) {
        for (i in 0 until this.childCount) {
            val view = this.getChildAt(i)
            if (view is TextView) {
                if (view.text == this.title) {
                    applyFont(view, font)
                    break
                }
            }
        }
    }

    private fun applyFont(tv: TextView, font: Typeface) {
        tv.typeface = font
    }

    override fun onDestroy() {
        RToast.cancel()
        super.onDestroy()
    }
}