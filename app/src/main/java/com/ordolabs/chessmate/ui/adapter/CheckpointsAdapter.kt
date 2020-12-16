package com.ordolabs.chessmate.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.ui.CheckpointItem
import com.ordolabs.chessmate.ui.adapter.base.BaseAdapter
import com.ordolabs.chessmate.ui.adapter.base.BaseViewHolder
import com.ordolabs.chessmate.ui.adapter.base.OnRecyclerItemClicksListener
import com.ordolabs.chessmate.util.viewId

class CheckpointsAdapter(
    clicksListener: OnRecyclerItemClicksListener
) : BaseAdapter<CheckpointItem, CheckpointsAdapter.CheckpointViewHolder>(clicksListener) {

    private val items = mutableListOf<CheckpointItem>()

    override fun setItems(items: List<CheckpointItem>) {
        this.items.addAll(items)
    }

    override fun onBindViewHolder(holder: CheckpointViewHolder, position: Int) {
        val item = items[position]
        holder.onBind(item)
    }

    override fun createViewHolder(itemView: View): CheckpointViewHolder {
        return CheckpointViewHolder(itemView)
    }

    fun add(checkpoint: CheckpointItem) {
        val position = items.size
        items.add(checkpoint)
        notifyItemInserted(position)
    }

    fun clear() {
        val itemCount = items.size
        items.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    override fun getItemViewLayout(viewType: Int): Int {
        return R.layout.item_checkpoint
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class CheckpointViewHolder(root: View) : BaseViewHolder<CheckpointItem>(root) {

        private val ordinal by root.viewId<TextView>(R.id.ordinal)
        private val player by root.viewId<TextView>(R.id.player_name)
        private val time by root.viewId<TextView>(R.id.checkpoint_time)
        private val expired by root.viewId<View>(R.id.badge_expired)

        override fun setViewsOnBind(item: CheckpointItem) {
            ordinal.text = item.ordinal.toString()
            player.text = item.playerName
            time.text = item.checkpointTime
            expired.isVisible = item.isExpired
        }
    }
}