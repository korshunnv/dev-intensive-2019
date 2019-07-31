package ru.skillbranch.devintensive.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import ru.skillbranch.devintensive.extensions.stripHtml
import kotlin.math.roundToLong

object Utils{

    fun parseFullName(fullName: String?): Pair<String?, String?> {
        var firstName:String? = null
        var lastName:String? = null
        if (!fullName.isNullOrEmpty()&& fullName != " "){
            val pats:List<String>? = fullName?.split(" ")
            firstName = pats?.getOrNull(0)
            lastName = pats?.getOrNull(1)
        }
        return  Pair(firstName, lastName)
    }

    fun transliteration(payload: String, divider:String = " "): String {
        val numbersMap = mapOf(
            "а" to "a", "б" to "b", "в" to "v", "г" to "g", "д" to "d", "е" to "e", "ё" to "e", "ж" to "zh",
            "з" to "z", "и" to "i", "й" to "i", "к" to "k", "л" to "l", "м" to "m", "н" to "n", "о" to "o",
            "п" to "p", "р" to "r", "с" to "s", "т" to "t", "у" to "u", "ф" to "f", "х" to "h", "ц" to "c",
            "ч" to "ch", "ш" to "sh", "щ" to "sh'", "ъ" to "", "ы" to "i", "ь" to "", "э" to "e", "ю" to "yu", "я" to "ya")

        var result: String = ""
            for (item in payload){
                if (item.toString() == " ")
                    result += divider
                else
                    if  (!numbersMap.containsKey(item.toLowerCase().toString())) {
                        result += item.toString()
                    } else {
                        if (item.toLowerCase().toString() == item.toString())
                            result += numbersMap[item.toString()]
                        else {
                            var temp = (numbersMap[item.toLowerCase().toString()])!!
                            if (temp.length>1)
                                result += temp.first().toUpperCase() + temp.last().toString()
                            else
                                result += temp.toUpperCase()
                        }
                    }
            }
            return result
        }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var result: String? = null
        if (!firstName.isNullOrEmpty() && firstName != " "){
            result = (firstName[0].toUpperCase()).toString()
        }
        if (!lastName.isNullOrEmpty() && lastName != " "){
            if (result==null) result = (lastName[0].toUpperCase()).toString()
            else result += (lastName[0].toUpperCase()).toString()
        }
        return result
    }

    fun convertDpToPx(context: Context, px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    fun dpToPx(context: Context, dp: Float): Long {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).roundToLong()
    }

}