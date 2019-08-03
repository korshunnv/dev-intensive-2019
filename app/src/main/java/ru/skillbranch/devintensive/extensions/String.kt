package ru.skillbranch.devintensive.extensions


fun String.truncate(limit: Int = 16): String {
    val tempString = this.trimEnd()
    return if (tempString.length <= limit) return tempString else tempString.dropLast(tempString.length - limit).trimEnd() + "..."
}

fun String.stripHtml(): String {
    val temp = (Regex("""<.+?>""").replace(this, ""))
    return (Regex(""" +""").replace(temp, " "))
}

fun String.validGithub(): Boolean {
    if (this.isNullOrEmpty()) {
        return true
    }   else {
        var pattern = """^(?:https://github.com|https://www.github.com|www.github.com|github.com)/(\w+)"""
        val matcherResult = Regex(pattern).find(this)
        if (matcherResult == null) {
            return false
        } else {
            val (name) = matcherResult.destructured
            val exceptionList: List<String> = listOf(
                "enterprise", "features", "topics", "collections", "trending", "events", "marketplace",
                "pricing", "nonprofit", "customer-stories", "security", "login", "join"
            )
            exceptionList.indexOf(name)
            return !(!Regex(pattern).matches(this) || exceptionList.indexOf(name) >= 0)
        }
    }
}