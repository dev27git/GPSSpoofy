package com.rp.basefiles

import androidx.recyclerview.widget.DiffUtil
import com.rp.util.adapter.RAdapterPayloadWatcher

interface IBaseAdapterPresenter<E> {

    val count: Int
    val all: List<E>

    fun onAttach(baseHolderView: IBaseHolderView)
    fun onAttachAdapter(adapter: BaseAdapter<*, *>)
    fun onAttachDiffCallback(callback: DiffUtil.ItemCallback<E>)

    fun onAttachPayloadWatcher(payloadWatcher: RAdapterPayloadWatcher<E>)

    fun update(list: List<E>)

    fun addNewList(listNewItems: List<E>)
    fun addItem(data: E)
    fun addItemAt(position: Int, data: E)

    fun removeItem(position: Int)

    fun changeItemAt(position: Int, data: E)

    fun getFrom(position: Int): E?

    fun onBind(position: Int)
    fun onBind(position: Int, payloads: Any)
}