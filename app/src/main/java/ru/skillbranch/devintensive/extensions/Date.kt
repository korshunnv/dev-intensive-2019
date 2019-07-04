package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern:String = "HH:mm:ss dd.MM.yy"):String{
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND):Date{
    var time =this.time
    time +=when (units){
        TimeUnits.SECOND -> value* SECOND
        TimeUnits.MINUTE -> value* MINUTE
        TimeUnits.HOUR -> value* HOUR
        TimeUnits.DAY -> value* DAY
    }
    this.time = time
    return this
}

//функция для склонения по падежам
private fun formatDateTime(value:Long, units: TimeUnits = TimeUnits.SECOND):String{
    var result = when (units) {
        TimeUnits.SECOND -> "секунд"
        TimeUnits.MINUTE -> "минут"
        TimeUnits.HOUR -> "часов"
        TimeUnits.DAY -> "дней"
    }
    if (value%10==1L && value%100!=11L) {
        result = when (units) {
            TimeUnits.SECOND -> "секунда"
            TimeUnits.MINUTE -> "минута"
            TimeUnits.HOUR -> "час"
            TimeUnits.DAY -> "день"
        }
    }
    else if (value%10L>=2L && value%10<=4L && value%100!=12L && value%100!=13L && value%100!=14L){
        result = when (units) {
            TimeUnits.SECOND -> "секунды"
            TimeUnits.MINUTE -> "минуты"
            TimeUnits.HOUR -> "часа"
            TimeUnits.DAY -> "дня"
        }
    }
    return result
}

fun Date.humanizeDiff(date: Date = Date()): String {
    var string = ""
    val time:Long = this.time - date.time
    val absTime:Long = if (time>0) time else time*(-1)
    val second = absTime / SECOND
        if (second<=1) string = "только что"
        else {
            if (second <= 45) string = "несколько секунд"
            else {
                if (second <= 75) string = "минуту"
                else {
                    val minute = absTime / MINUTE
                    if (minute <= 45) string = "$minute ${formatDateTime(minute, TimeUnits.MINUTE)}"
                    else {
                        if (minute <= 75) string = "час"
                        else {
                            val hour = absTime / HOUR
                            if (hour <= 22) string = "$hour ${formatDateTime(hour, TimeUnits.HOUR)}"
                            else {
                                if (hour < 26) string = "день"
                                else {
                                    val day = absTime / DAY
                                    if (day < 360) string = "$day ${formatDateTime(day, TimeUnits.DAY)}"
                                    else string = "более года"
                                }
                            }
                        }
                    }
                }
            }
        }
    if (!string.equals("только что"))
        if (time>0 && time/ DAY>=360 ) string = "более чем через год"
        else string = if (time<0) string+ " назад"  else "через "+string
    return string
}

enum class TimeUnits{
    SECOND {
        override fun plural(value: Long): String {
            return "$value ${formatDateTime(value, SECOND)}"
        }
    },
    MINUTE {
        override fun plural(value: Long): String {
            return "$value ${formatDateTime(value, MINUTE)}"
        }
    },
    HOUR {
        override fun plural(value: Long): String {
            return "$value ${formatDateTime(value, HOUR)}"
        }
    },
    DAY {
        override fun plural(value: Long): String {
            return "$value ${formatDateTime(value, DAY)}"
        }
    };

    abstract fun plural(value:Long): String
}