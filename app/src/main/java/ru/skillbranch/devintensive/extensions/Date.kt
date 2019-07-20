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
            TimeUnits.SECOND -> "секунду"
            TimeUnits.MINUTE -> "минуту"
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
    var timeString = ""
    val time:Long = this.time - date.time
    val absTime:Long = if (time>0) time else time*(-1)
    val second = absTime / SECOND
        if (second<=1) timeString = "только что"
        else {
            if (second <= 45) timeString = "несколько секунд"
            else {
                if (second <= 75) timeString = "минуту"
                else {
                    val minute = absTime / MINUTE
                    if (minute <= 45) timeString = "$minute ${formatDateTime(minute, TimeUnits.MINUTE)}"
                    else {
                        if (minute <= 75) timeString = "час"
                        else {
                            val hour = absTime / HOUR
                            if (hour <= 22) timeString = "$hour ${formatDateTime(hour, TimeUnits.HOUR)}"
                            else {
                                if (hour < 26) timeString = "день"
                                else {
                                    val day = absTime / DAY
                                    if (day < 360) timeString = "$day ${formatDateTime(day, TimeUnits.DAY)}"
                                    else timeString = "более года"
                                }
                            }
                        }
                    }
                }
            }
        }
    if (!timeString.equals("только что"))
        if (time>0 && time/ DAY>=360 ) timeString = "более чем через год"
        else timeString = if (time<0) timeString+ " назад"  else "через "+timeString
    return timeString
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