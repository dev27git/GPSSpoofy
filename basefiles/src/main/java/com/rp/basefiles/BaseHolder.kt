package com.rp.basefiles

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder<P : IBaseAdapterPresenter<*>>(itemView: View)
    : RecyclerView.ViewHolder(itemView), IBaseHolderView {

    lateinit var presenter: P

    override val view: IBaseHolderView
        get() = this
}