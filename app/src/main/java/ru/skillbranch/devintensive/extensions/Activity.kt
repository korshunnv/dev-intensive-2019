package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager




fun Activity.hideKeyboard(){
    if (currentFocus != null){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
    }
}

fun Activity.hideSoftKeyboard() {
    if (currentFocus != null) {
        val inputMethodManager = getSystemService(Context
            .INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}
