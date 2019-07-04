package ru.skillbranch.devintensive.extensions


fun String.truncate(limit: Int = 16):String{
    return if (this.trimEnd().length<=limit) return this.trimEnd() else this.dropLast(this.length-limit).trimEnd()+"..."
}

/*
Реализуй extension позволяющий очистить строку от html тегов и html escape последовательностей ("& < > ' ""), а так же удалить пустые символы (пробелы) между словами если их больше 1. Необходимо вернуть модифицированную строку
Пример:
"<p class="title">Образовательное IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch
"<p>Образовательное       IT-сообщество Skill Branch</p>".stripHtml() //Образовательное IT-сообщество Skill Branch
 */

fun String.stripHtml():String{
    val temp = (Regex("""<.+?>""").replace(this, ""))
    return  (Regex(""" +""").replace(temp, " "))
}