package com.ordolabs.chessmate.ui.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder
import kotlinx.android.synthetic.main.fragment_home_tab_clock.view.*

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
        val bgColor = ResourcesCompat.getColor(
            resources, R.color.timer_warn_color, context.theme
        )
        background = ColorDrawable(bgColor)
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
        animShowHideCollapsed(show = true).apply {
            doOnStart {
                isVisible = true
            }
            doOnEnd {
                constraintTo(ConstraintSet.END)
            }
            start()
        }
    }

    private fun hideCollapsed() {
        animShowHideCollapsed(show = false).apply {
            doOnEnd {
                isVisible = false
                constraintTo(ConstraintSet.START)
            }
            start()
        }
    }

    private fun expand() {

    }

    private fun hideExpanded() {

    }

    private fun constraintTo(anchor: Int) {
        val cl = parent as ConstraintLayout
        val constraints = ConstraintSet().apply {
            clone(cl)
        }
        val currentAnchor = if (anchor == ConstraintSet.START) {
            ConstraintSet.END
        } else {
            ConstraintSet.START
        }
        constraints.clear(id, currentAnchor)
        constraints.connect(id, anchor, timerViewId, anchor)
        constraints.applyTo(cl)
    }

    private fun animShowHideCollapsed(show: Boolean) = ValueAnimatorBuilder.of<Int>(show) {
        looped { false }
        values {
            if (show) {
                val timerViewWidth = rootView.findViewById<View>(timerViewId).width
                arrayOf(initialSize, timerViewWidth)
            } else {
                arrayOf(this@TimerWarnView.width, initialSize)
            }
        }
        updateListener {
            this@TimerWarnView.updateLayoutParams {
                width = animatedValue as Int
            }
        }
    }

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