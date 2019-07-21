package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager






fun Activity.hideKeyboard(){
    //if (currentFocus != null){
    if (isKeyboardOpen()){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
    }
}

/*
**Activity.isKeyboardOpen Activity.isKeyboardClosed
Реализуй extension для проверки, открыта или нет Software Keyboard с применением метода rootView.getWindowVisibleDisplayFrame(Rect())
 */
fun Activity.isKeyboardOpen():Boolean{
    val r = Rect()
    val rootview = this.window.decorView // this = activity
    rootview.getWindowVisibleDisplayFrame(r)
    return r.height()>0
}

fun Activity.isKeyboardClosed():Boolean{
    val r = Rect()
    val rootview = this.window.decorView // this = activity
    rootview.getWindowVisibleDisplayFrame(r)
    return r.height()<=0
}