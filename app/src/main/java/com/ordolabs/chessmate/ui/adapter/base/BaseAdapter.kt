package com.ordolabs.chessmate.ui.adapter.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * An abstract implementation of [RecyclerView.Adapter].
 */
abstract class BaseAdapter<T : Any, VH : BaseViewHolder<T>>(
    var clicksListener: OnRecyclerItemClicksListener
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val root = createItemView(parent, viewType)
        val holder = createViewHolder(root)
        setViewHolder(holder, viewType)
        return holder
    }

    override fun onViewAttachedToWindow(holder: VH) {
        super.onViewAttachedToWindow(holder)
    }

    /**
     * Should be called to set data into `Adapter`.
     */
    abstract fun setItems(items: List<T>)

    /**
     * Specifies way of creating instances of [VH].
     */
    protected abstract fun createViewHolder(itemView: View): VH

    /**
     * Specifies layout ID res, according to [viewType].
     */
    @LayoutRes
    protected abstract fun getItemViewLayout(viewType: Int): Int

    /**
     * Configurates specified [holder] and its view.
     */
    @CallSuper
    protected open fun setViewHolder(holder: VH, viewType: Int) {
        holder.itemView.setOnClickListener { onViewHolderClick(holder) }
        holder.itemView.setOnLongClickListener { onViewHolderLongClick(holder) }
    }

    /**
     * Would be called on [VH]'s view click.
     */
    private fun onViewHolderClick(holder: VH) {
        performClick(holder)
        holder.onClick(holder.itemView)
        clicksListener.onRecyclerItemClick(holder.bindingAdapterPosition)
    }

    /**
     * Would be called on [VH]'s view long click.
     */
    private fun onViewHolderLongClick(holder: VH): Boolean {
        performLongClick(holder)
        val consumed = holder.onLongClick(holder.itemView)
        clicksListener.onRecyclerItemLongClick(holder.bindingAdapterPosition)
        return consumed
    }

    open fun performClick(holder: VH) {
        // default empty implementation
    }

    open fun performLongClick(holder: VH) {
        // default empty implementation
    }

    /**
     * Inflates [getItemViewLayout] view.
     */
    protected open fun createItemView(parent: ViewGroup, viewType: Int): View {
        val layout = getItemViewLayout(viewType)
        return LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }

    /**
     * @return `Unit`, if specified [index] is in [getItemCount] range,
     * `null` otherwise.
     */
    protected fun ensureIndexInItemsRange(index: Int): Unit? {
        if (index in 0 until itemCount) return Unit
        return null
    }
}