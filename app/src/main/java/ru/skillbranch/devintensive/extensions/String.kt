package ru.skillbranch.devintensive.extensions


fun String.truncate(limit: Int = 16): String {
    val tempString = this.trimEnd()
    return if (tempString.length <= limit) return tempString else tempString.dropLast(tempString.length - limit).trimEnd() + "..."
}

fun String.stripHtml(): String {
    val temp = (Regex("""<.+?>""").replace(this, ""))
    return (Regex(""" +""").replace(temp, " "))
}

/*
Реализуй валидацию вводимых пользователем данных в поле @id/et_repository на соответствие url валидному github аккаунту,
вводимое значение должно содержать домен github.com (https://, www, https://www) и
аккаунт пользователя (пути для исключения прикреплены в ресурсах урока).
Если URL невалиден, выводить сообщение "Невалидный адрес репозитория" в TextInputLayout (wr_repository.error(message)) и
запрещать сохранение невалидного значения в SharedPreferences (при попытке сохранить невалидное поле очищать et_repository при нажатии @id/btn_edit)
Пример:
https://github.com/johnDoe //валиден
https://www.github.com/johnDoe //валиден
www.github.com/johnDoe //валиден
github.com/johnDoe //валиден
https://anyDomain.github.com/johnDoe //невалиден
https://github.com/ //невалиден
https://github.com //невалиден
https://github.com/johnDoe/tree //невалиден
https://github.com/johnDoe/tree/something //невалиден
https://github.com/enterprise //невалиден
https://github.com/pricing //невалиден
https://github.com/join //невалиден
 */

fun String.validGithub(): Boolean {
    if (this.isNullOrEmpty()) {
        return true
    }   else {
        var pattern = """^(?:https://github.com|https://www.github.com|www.github.com|github.com)/(\w+)"""
        val matcherResult = Regex(pattern).find(this)
        if (matcherResult == null) {
            return false
        } else {
            val (name) = matcherResult!!.destructured
            val exceptionList: List<String> = listOf(
                "enterprise", "features", "topics", "collections", "trending", "events", "marketplace",
                "pricing", "nonprofit", "customer-stories", "security", "login", "join"
            )
            exceptionList.indexOf(name)
            return !(!Regex(pattern).matches(this) || exceptionList.indexOf(name) >= 0)
        }
    }
}