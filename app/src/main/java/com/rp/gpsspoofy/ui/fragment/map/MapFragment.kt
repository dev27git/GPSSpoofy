package com.rp.gpsspoofy.ui.fragment.map


import android.os.Bundle
import android.view.View
import com.dev.rahul.imago.base.BaseFragment
import com.rp.gpsspoofy.R


class MapFragment : BaseFragment() {

    val TAG = MapFragment::class.java.simpleName

    override val layoutRes: Int
        get() = R.layout.fragment_map

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onRefresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
