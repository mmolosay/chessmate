package com.ordolabs.chessmate.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder

class TimerWarnView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var state = State.HIDDEN
        private set

    private val timerViewId: Int
    private val initialSize: Int by lazy {
        layoutParams.width
    }

    init {
        background = ResourcesCompat.getDrawable(
            resources, R.drawable.bg_timer_warn, context.theme
        )
        isVisible = false

        context.obtainStyledAttributes(attrs, R.styleable.TimerWarnView).apply {
            timerViewId = getResourceIdOrThrow(R.styleable.TimerWarnView_timer_view)
        }.recycle()
    }

    fun setState(state: State) {
        if (this.state == state) return
        applyNewState(state)
    }

    private fun applyNewState(newState: State) {
        when (state) {
            State.HIDDEN -> when (newState) {
                State.COLLAPSED -> showCollapsed()
                State.EXPANDED -> throw IllegalNewStateArgumentException()
                State.HIDDEN -> throw IllegalStateException("How did you get here?")
            }
            State.COLLAPSED -> when (newState) {
                State.EXPANDED -> expand()
                State.HIDDEN -> hideCollapsed()
                State.COLLAPSED -> throw IllegalStateException("How did you get here?")
            }
            State.EXPANDED -> when (newState) {
                State.HIDDEN -> hideExpanded()
                State.COLLAPSED -> throw IllegalNewStateArgumentException()
                State.EXPANDED -> throw IllegalStateException("How did you get here?")
            }
        }
        this.state = newState
    }

    private fun showCollapsed() {
        isVisible = true
        animAppearingAndCollapsing()
    }

    private fun hideCollapsed() {

    }

    private fun expand() {

    }

    private fun hideExpanded() {

    }

    private fun animAppearingAndCollapsing() = ValueAnimatorBuilder.of<Int>(isForward = true) {
        values {
            val timerViewWidth = (parent as ViewGroup).findViewById<View>(timerViewId).width
            arrayOf(initialSize, timerViewWidth)
        }
        updateListener {
            this@TimerWarnView.updateLayoutParams {
                width = animatedValue as Int
            }
        }
    }.start()

    enum class State {
        HIDDEN, COLLAPSED, EXPANDED
    }

    companion object {
        const val WARN_APPEAR_PREEMPTION = 5000L
    }

    private inner class IllegalNewStateArgumentException : IllegalArgumentException() {
        override val message: String = "You can not set this state, when it is ${state.name}"
    }
}