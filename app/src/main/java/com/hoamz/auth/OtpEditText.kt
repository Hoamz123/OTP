package com.hoamz.auth

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

class OtpEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    var onBackspaceListener: (() -> Unit)? = null

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && text.isNullOrEmpty()) {
            onBackspaceListener?.invoke()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && text.isNullOrEmpty()) {
            onBackspaceListener?.invoke()
            return true
        }
        return super.dispatchKeyEvent(event)
    }
}
