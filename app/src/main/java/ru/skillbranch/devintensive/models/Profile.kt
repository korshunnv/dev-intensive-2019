package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils

data class Profile(
    val firstName: String,
    val lastName: String,
    val about: String,
    val repository: String,
    val rating: Int = 0,
    val respect: Int = 0,
    val nickName:String = "John Doe"//implement me
) {
    //val nickName:String = "Dohn Doe"//implement me
    val rank: String = "Junior Android Developer"


    fun toMap():Map<String, Any> = mapOf(
        "nickName" to nickName,
        "rank" to rank,
        "firstName" to firstName,
        "lastName" to lastName,
        "about" to about,
        "repository" to repository,
        "rating" to rating,
        "respec" to respect
    )

    fun nickName():String{
        return Utils.transliteration(firstName, lastName)
    }
}