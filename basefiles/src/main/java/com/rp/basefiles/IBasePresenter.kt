package com.rp.basefiles

interface IBasePresenter<V : IBaseView> {

    fun onAttach(baseView: V)
    fun onDestroy()
}
