package com.rp.gpsspoofy.ui.fragment.map

import com.rp.basefiles.IBasePresenter
import com.rp.basefiles.IBaseView

interface MapMVP {

    interface IView : IBaseView {
        fun showMockLocation(latitude: Double, longitude: Double)
        fun setNewLocationOnMap(latitude: Double, longitude: Double)
    }

    interface IPresenter : IBasePresenter<IView> {

    }
}