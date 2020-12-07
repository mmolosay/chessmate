package com.ordolabs.chessmate.util

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

// do not remove explicit type argument — causes build crash

fun <T> lazyUnsafe(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : View> View.viewId(@IdRes id: Int): Lazy<T> =
    lazyUnsafe { this.findViewById<T>(id) }

fun <T : View> Fragment.viewId(@IdRes id: Int): Lazy<T> =
    lazyUnsafe { requireView().findViewById<T>(id) }

fun <T : View> AppCompatActivity.viewId(@IdRes id: Int): Lazy<T> =
    lazyUnsafe { findViewById<T>(id) }

fun <T : View> RecyclerView.ViewHolder.viewId(@IdRes id: Int): Lazy<T> =
    lazyUnsafe { itemView.findViewById<T>(id) }
