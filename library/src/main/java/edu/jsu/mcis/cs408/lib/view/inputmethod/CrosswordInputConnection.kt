package edu.jsu.mcis.cs408.crossword.lib.view.inputmethod

import android.view.View
import android.view.inputmethod.BaseInputConnection


class CrosswordInputConnection(targetView: View) : BaseInputConnection(targetView, false) {

    var onInputEventListener: OnInputEventListener? = null

    interface OnInputEventListener {
        fun onWordEntered(text: CharSequence)
        fun onWordCancelled()
        fun onEditorAction(actionCode: Int)
    }

    override fun commitText(text: CharSequence, newCursorPosition: Int): Boolean {
        if (text.isEmpty()) onInputEventListener?.onWordCancelled()
        return super.commitText(text, newCursorPosition)
    }

    override fun performEditorAction(actionCode: Int): Boolean {
        onInputEventListener?.onEditorAction(actionCode)
        return super.performEditorAction(actionCode)
    }

    override fun setComposingText(text: CharSequence, newCursorPosition: Int): Boolean {
        onInputEventListener?.onWordEntered(text)
        return super.setComposingText(text, newCursorPosition)
    }
}
