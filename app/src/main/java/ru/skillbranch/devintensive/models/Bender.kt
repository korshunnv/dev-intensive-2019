package ru.skillbranch.devintensive.models

class Bender (var status:Status = Status.NORMAL, var question:Question = Question.NAME) {

    fun askQueston(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    /*
    *Activity.hideKeyboard
    Требования к методу listenAnswer:
    При вводе неверного ответа изменить текущий статус на следующий статус (status = status.nextStatus()),
    вернуть "Это неправильный ответ\n${question.question}" to status.color и
    изменить цвет ImageView (iv_bender) на цвет status.color (метод setColorFilter(color,"MULTIPLY"))

    При вводе неверного ответа более 3 раз сбросить состояние сущности Bender на значение по умолчанию (status = Status.NORMAL, question = Question.NAME)
    и вернуть "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color и изменить цвет ImageView (iv_bender) на цвет status.color
    Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity (достаточно сохранить Status, Question)
     */

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {

        var validString = question.validationAnswer(answer)
        return if (validString == null){
            if (question == Question.IDLE){
                "\n${question.question}" to status.color
            } else {
                if (question.answer.contains(answer.toLowerCase())) {
                    question = question.nextQUESTION()
                    validString = "Отлично - ты справился"
                    validString + "\n${question.question}" to status.color
                } else {
                    if (status<Status.CRITICAL) {
                        status = status.nextStatus()
                        "Это неправильный ответ\n${question.question}" to status.color
                    } else {
                        status = Status.NORMAL
                        question = Question.NAME
                        "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
                    }
                }
            }
        } else validString + "\n${question.question}" to status.color
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answer: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun validationAnswer(newAnswer: String): String? {
                return if (!newAnswer.get(0).isUpperCase()) {
                    "Имя должно начинаться с заглавной буквы"
                }
                else {
                    null
                }
            }

            override fun nextQUESTION(): Question = PROFESSION

        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun validationAnswer(newAnswer: String): String? {
                return if (!newAnswer[0].isLowerCase())
                    "Профессия должна начинаться со строчной буквы"
                else null
            }

            override fun nextQUESTION(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun validationAnswer(newAnswer: String): String? {
                val flag = (Regex("""\d""").containsMatchIn(newAnswer))
                return if (flag)
                    "Материал не должен содержать цифр"
                else null
            }

            override fun nextQUESTION(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun validationAnswer(newAnswer: String): String? {
                val flag = (Regex("""(\d){1,}""").matches(newAnswer))
                return if (!flag)
                    "Год моего рождения должен содержать только цифры"
                else null
            }

            override fun nextQUESTION(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun validationAnswer(newAnswer: String): String? {
                val flag = (Regex("""(\d){7}""").matches(newAnswer))
                return if (!flag)
                    "Серийный номер содержит только цифры, и их 7"
                else null
            }

            override fun nextQUESTION(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun validationAnswer(newAnswer: String): String? = null

            override fun nextQUESTION(): Question = IDLE
        };

        abstract fun nextQUESTION(): Question
        abstract fun validationAnswer(newAnswer: String):String?
    }

}
