package com.ordolabs.chessmate.ui.view

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
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

class TimerWarnView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var state = State.HIDDEN
        private set

    private val timerViewId: Int
    private val initialWidth by lazy { layoutParams.width }
    private val initialHeight by lazy { layoutParams.height }
    private val initialTranslationY by lazy { translationY }

    init {
        val bgColor = ResourcesCompat.getColor(
            resources, R.color.timer_warn, context.theme
        )
        background = ColorDrawable(bgColor)
        isVisible = false

        context.obtainStyledAttributes(attrs, R.styleable.TimerWarnView).apply {
            timerViewId = getResourceIdOrThrow(R.styleable.TimerWarnView_timer_view)
        }.recycle()

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
        }
        setLayerType(LAYER_TYPE_SOFTWARE, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        initialWidth; initialHeight; initialTranslationY
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
                State.COLLAPSED -> collapse()
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
                setConstraintBias(LinearLayout.HORIZONTAL, 1f)
            }
            doOnEnd {
                isVisible = false
                setConstraintBias(LinearLayout.HORIZONTAL, 0f)
            }
            start()
        }
    }

    private fun expand() {
        animExpandHide(expand = true).apply {
            start()
        }
    }

    private fun collapse() {
        animCollapseFromExpanded().apply {
            start()
        }
    }

    private fun hideExpanded() {
        animExpandHide(expand = false).apply {
            doOnStart {
                setConstraintBias(LinearLayout.VERTICAL, 0f)
            }
            doOnEnd {
                isVisible = false
                translationY = initialTranslationY
                setConstraintBias(LinearLayout.VERTICAL, 1f)
            }
            start()
        }
    }

    private fun setConstraintBias(axis: Int, bias: Float) {
        val cl = parent as ConstraintLayout
        ConstraintSet().apply {
            clone(cl)
            if (axis == LinearLayout.HORIZONTAL) {
                setHorizontalBias(id, bias)
            }
            if (axis == LinearLayout.VERTICAL) {
                setVerticalBias(id, bias)
            }
            applyTo(cl)
        }
    }

    private fun animShowHideCollapsed(show: Boolean) = ValueAnimatorBuilder.of<Int>(show) {
        looped { false }
        values {
            if (show) {
                arrayOf(initialWidth, getTimerView().width)
            } else {
                arrayOf(width, initialWidth)
            }
        }
        updateListener {
            updateLayoutParams {
                width = animatedValue as Int
            }
        }
    }

    private fun animExpandHide(expand: Boolean) = AnimatorSet().apply {
        playTogether(
            ValueAnimatorBuilder.of<Int>(expand) {
                looped { false }
                values {
                    if (expand) {
                        arrayOf(height, getTimerView().height)
                    } else {
                        arrayOf(height, initialHeight)
                    }
                }
                updateListener {
                    updateLayoutParams {
                        height = animatedValue as Int
                    }
                }
            },
            ValueAnimatorBuilder.of<Float>(expand) {
                looped { false }
                values {
                    if (expand) {
                        arrayOf(initialTranslationY, 0f)
                    } else {
                        arrayOf(0f, 0f)
                    }
                }
                updateListener {
                    updateLayoutParams {
                        translationY = animatedValue as Float
                    }
                }
            }
        )
    }

    private fun animCollapseFromExpanded() = AnimatorSet().apply {
        playTogether(
            ValueAnimatorBuilder.of<Int>(true) {
                values {
                    arrayOf(height, initialHeight)
                }
                updateListener {
                    updateLayoutParams {
                        height = animatedValue as Int
                    }
                }
            },
            ValueAnimatorBuilder.of<Float>(true) {
                values {
                    arrayOf(translationY, initialTranslationY)
                }
                updateListener {
                    translationY = animatedValue as Float
                }
            }
        )
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