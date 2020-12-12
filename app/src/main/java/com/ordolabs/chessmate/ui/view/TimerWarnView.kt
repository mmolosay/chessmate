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
    private val initialWidth by lazy { layoutParams.width }
    private val initialHeight by lazy { layoutParams.height }

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
            start()
        }
    }

    private fun hideCollapsed() {
        animShowHideCollapsed(show = false).apply {
            doOnStart {
                constraintTo(ConstraintSet.END)
            }
            doOnEnd {
                isVisible = false
                constraintTo(ConstraintSet.START)
            }
            start()
        }
    }

    private fun expand() {
        animExpandHide(expand = true).apply {
            doOnStart {
                constraintTo(ConstraintSet.BOTTOM)
            }
            doOnEnd {
                constraintTo(ConstraintSet.TOP)
            }
            start()
        }
    }

    private fun hideExpanded() {
        animExpandHide(expand = false).apply {
            doOnEnd {
                isVisible = false
                getConstratins().apply {
                    clear(id, ConstraintSet.TOP)
                    connect(id, ConstraintSet.TOP, timerViewId, ConstraintSet.BOTTOM)
                    applyTo(parent as ConstraintLayout)
                }
            }
            start()
        }
    }

    private fun getConstratins(): ConstraintSet {
        return ConstraintSet().apply {
            clone(parent as ConstraintLayout)
        }
    }

    private fun constraintTo(anchor: Int) {
        val prevAnchor = when (anchor) {
            ConstraintSet.START -> ConstraintSet.END
            ConstraintSet.END -> ConstraintSet.START
            ConstraintSet.TOP -> ConstraintSet.BOTTOM
            ConstraintSet.BOTTOM -> ConstraintSet.TOP
            else -> throw IllegalArgumentException()
        }
        getConstratins().apply {
            clear(id, prevAnchor)
            connect(id, anchor, timerViewId, anchor)
            applyTo(parent as ConstraintLayout)
        }
    }

    private fun animShowHideCollapsed(show: Boolean) = ValueAnimatorBuilder.of<Int>(show) {
        looped { false }
        values {
            if (show) {
                val timerViewWidth = getTimerView().width
                arrayOf(initialWidth, timerViewWidth)
            } else {
                arrayOf(this@TimerWarnView.width, initialWidth)
            }
        }
        updateListener {
            this@TimerWarnView.updateLayoutParams {
                width = animatedValue as Int
            }
        }
    }

    private fun animExpandHide(expand: Boolean) = ValueAnimatorBuilder.of<Int>(expand) {
        looped { false }
        values {
            if (expand) {
                initialHeight // initialization
                val timerViewHeight = getTimerView().height
                arrayOf(height, timerViewHeight)
            } else {
                arrayOf(height, initialHeight)
            }
        }
        updateListener {
            this@TimerWarnView.updateLayoutParams {
                height = animatedValue as Int
            }
        }
    }

    private fun getTimerView(): View {
        return rootView.findViewById(timerViewId)
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