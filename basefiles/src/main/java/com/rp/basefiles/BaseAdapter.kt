package com.rp.basefiles

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.InvocationTargetException


abstract class BaseAdapter<E, P : IBaseAdapterPresenter<E>>(private val presenter: P)
    : RecyclerView.Adapter<BaseHolder<P>>() {

    private val TAG : String = BaseAdapter::class.java.simpleName

    init {
        presenter.onAttachAdapter(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<P> {
        val inflate = LayoutInflater.from(parent.context).inflate(getLayoutRes(viewType), parent, false)
        val str = getClassHolder(viewType).name
        var baseHolder: BaseHolder<P>? = null
        try {
            val aClass = Class.forName(str)
            baseHolder = aClass.getConstructor(View::class.java).newInstance(inflate) as BaseHolder<P>?
            baseHolder?.presenter = presenter
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return baseHolder as BaseHolder<P>
    }

    override fun onBindViewHolder(holder: BaseHolder<P>, position: Int) {
        Log.e(TAG, "onBindViewHolder: called")
        presenter.onAttach(holder.view)
        presenter.onBind(position)
    }

    override fun onBindViewHolder(holder: BaseHolder<P>, position: Int, payloads: MutableList<Any>) {
        Log.e(TAG, "onBindViewHolder: payload called")
        if (payloads.isEmpty())
            onBindViewHolder(holder, position)
        else {
            presenter.onAttach(holder.view)
            Log.e(TAG, "onBindViewHolder: payload Size => " + payloads.size
                    + " position : " + position + " payload value : " + payloads[0])
            presenter.onBind(position, payloads[0])
        }
    }

    override fun getItemCount(): Int = presenter.count

    override fun getItemViewType(position: Int): Int = getViewType(position)

    override fun getItemId(position: Int): Long = position.toLong()

    fun presenter(): P = presenter

    abstract fun getLayoutRes(viewType: Int): Int
    abstract fun getClassHolder(viewType: Int): Class<*>
    abstract fun getViewType(position: Int): Int
}