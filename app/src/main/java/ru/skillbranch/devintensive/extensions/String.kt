package ru.skillbranch.devintensive.extensions


fun String.truncate(limit: Int = 16):String{
    val tempString = this.trimEnd()
    return if (tempString.length<=limit) return tempString else tempString.dropLast(tempString.length-limit).trimEnd()+"..."
}

fun String.stripHtml():String{
    val temp = (Regex("""<.+?>""").replace(this, ""))
    return  (Regex(""" +""").replace(temp, " "))
}