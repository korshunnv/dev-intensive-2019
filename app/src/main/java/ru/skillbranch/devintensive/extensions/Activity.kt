package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import android.view.View
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.utils.Utils.convertDpToPx
import java.lang.Math.abs
import kotlin.math.roundToLong


fun Activity.hideKeyboard(){
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.SHOW_FORCED)
}

fun Activity.isKeyboardOpen(): Boolean {
    val rect = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(rect)
    val heightDiff = rootView.height - rect.height()
    val error = convertDpToPx(this,50)//this.dpToPx(50F)

    return heightDiff > error
}

/*fun Activity.isKeyboardOpen():Boolean{
    val rect = Rect()
    val rootView = findViewById<View>(android.R.id.content)
    rootView.getWindowVisibleDisplayFrame(rect)

    val heightDiff = abs(rootView.height - (rect.bottom-rect.top))

    return heightDiff < rootView.height/3;
}*/

fun Activity.isKeyboardClosed():Boolean{
    return this.isKeyboardOpen().not()
}