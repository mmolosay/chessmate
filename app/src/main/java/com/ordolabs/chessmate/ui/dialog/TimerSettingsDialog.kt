package com.ordolabs.chessmate.ui.dialog

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.animation.doOnEnd
import androidx.core.widget.doAfterTextChanged
import com.ordolabs.chessmate.R
import com.ordolabs.chessmate.model.presentation.TimerSettingsPresentation
import com.ordolabs.chessmate.util.wrapper.ValueAnimatorBuilder
import kotlinx.android.synthetic.main.dialog_timer_settings.*

class TimerSettingsDialog(
    private val settings: TimerSettingsPresentation,
    private val onDialogApplied: (TimerSettingsPresentation) -> Unit
) : BaseDialogFragment() {

    private var arePlayersSwapped = false
    private val playerViewsDistance by lazy {
        edit_player2.y - edit_player1.y
    }

    override fun getDialogLayoutRes(): Int {
        return R.layout.dialog_timer_settings
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        setSettingsInViews()
        setTimerLimitCoercer(edit_timer_limit_minutes, 0..59)
        setTimerLimitCoercer(edit_timer_limit_seconds, 5..59)
        setSwapPlayersButton()
        setOkButton()
    }

    private fun setSettingsInViews() {
        edit_timer_limit_minutes.setText(settings.limitMinutes.toString())
        edit_timer_limit_seconds.setText(settings.limitSeconds.toString())
        edit_player1.setText(settings.player1)
        edit_player2.setText(settings.player2)
    }

    private fun setSwapPlayersButton() {
        btn_swap_players.setOnClickListener {
            AnimatorSet().apply {
                playTogether(
                    animPlayerNameSwap(edit_player1, false),
                    animPlayerNameSwap(edit_player2, true)
                )
                doOnEnd {
                    arePlayersSwapped = !arePlayersSwapped
                }
            }.start()
        }
    }

    private fun setOkButton() {
        btn_apply.setOnClickListener {
            val settings = collectSettings()
            onDialogApplied(settings)
            dismiss()
        }
    }

    private fun collectSettings(): TimerSettingsPresentation {
        val minutes = edit_timer_limit_minutes.text.toString().toInt()
        val seconds = edit_timer_limit_seconds.text.toString().toInt()
        val player1 = if (!arePlayersSwapped) edit_player1.text.toString()
        else edit_player2.text.toString()
        val player2 = if (!arePlayersSwapped) edit_player2.text.toString()
        else edit_player1.text.toString()
        return TimerSettingsPresentation(minutes, seconds, player1, player2)
    }

    private fun setTimerLimitCoercer(edit: EditText, range: IntRange) {
        edit.doAfterTextChanged { text ->
            if (text.isNullOrBlank()) return@doAfterTextChanged
            val number = text.toString().toInt()
            val coerce = number.coerceIn(range)
            if (number != coerce) {
                edit.setText(coerce.toString())
            }
        }
    }

    private fun animPlayerNameSwap(playerNameView: View, goesUp: Boolean) =
        ValueAnimatorBuilder.of<Float>(!arePlayersSwapped) {
            values {
                arrayOf(0f, playerViewsDistance * if (goesUp) -1 else 1)
            }
            duration {
                400L
            }
            updateListener {
                playerNameView.translationY = animatedValue as Float
            }
        }

    companion object {
        fun new(
            settings: TimerSettingsPresentation,
            onApplied: (TimerSettingsPresentation) -> Unit
        ): TimerSettingsDialog {
            return TimerSettingsDialog(settings, onApplied)
        }
    }
}