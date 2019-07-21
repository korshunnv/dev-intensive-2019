package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.opengl.ETC1.getHeight
import android.R.attr.top
import android.R.attr.bottom
import android.content.pm.ActivityInfo
import android.graphics.drawable.GradientDrawable
import android.icu.text.IDNA
import androidx.appcompat.widget.LinearLayoutCompat
import kotlin.math.abs


fun Activity.hideKeyboard(){
    //if (currentFocus != null){
    //if (isKeyboardOpen()){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
    //}
}

/*
**Activity.isKeyboardOpen Activity.isKeyboardClosed
Реализуй extension для проверки, открыта или нет Software Keyboard с применением метода rootView.getWindowVisibleDisplayFrame(Rect())
 */
fun Activity.isKeyboardOpen():Boolean{
    val rect = Rect()
    this.window.decorView.getWindowVisibleDisplayFrame(rect) // this = activity
    return this.window.decorView.height>rect.height()
}

fun Activity.isKeyboardClosed():Boolean{
    return ! isKeyboardOpen()
}