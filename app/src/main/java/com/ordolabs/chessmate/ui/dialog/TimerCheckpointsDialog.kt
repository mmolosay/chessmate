package com.ordolabs.chessmate.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.ui.adapter.CheckpointItem
import com.ordolabs.chessmate.ui.adapter.CheckpointsAdapter
import com.ordolabs.chessmate.ui.adapter.base.OnRecyclerItemClicksListener
import kotlinx.android.synthetic.main.dialog_checkpoints_list.*

class TimerCheckpointsDialog(
    private val list: List<CheckpointItem>
) : BaseDialogFragment() {

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_checkpoints_list
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        setCheckpointsList()
    }

    private fun setCheckpointsList() {
        val lm = LinearLayoutManager(context)
        val adapter = CheckpointsAdapter(object : OnRecyclerItemClicksListener {}).apply {
            setItems(list)
        }
        checkpoints.adapter = adapter
        checkpoints.layoutManager = lm
    }

    companion object {
        fun new(list: List<CheckpointItem>): TimerCheckpointsDialog {
            return TimerCheckpointsDialog(list)
        }
    }
}