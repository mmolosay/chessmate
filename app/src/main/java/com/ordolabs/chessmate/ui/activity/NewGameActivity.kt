package com.ordolabs.chessmate.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ordolabs.chessmate.R

class NewGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
    }

    companion object {
        fun getStartingIntent(context: Context): Intent {
            return Intent(context, NewGameActivity::class.java)
        }
    }
}