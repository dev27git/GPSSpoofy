package com.rp.basefiles

abstract class BasePresenter<V : IBaseView> : IBasePresenter<V> {

    private var view: V? = null

    override fun onAttach(baseView: V) {
        this.view = baseView
    }

    fun view(): V? = view

    override fun onDestroy() {
        view = null
    }
}