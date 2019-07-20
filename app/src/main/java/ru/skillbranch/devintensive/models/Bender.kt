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
    Требования к методу listenAnswer:
    При вводе неверного ответа изменить текущий статус на следующий статус (status = status.nextStatus()),
    вернуть "Это неправильный ответ\n${question.question}" to status.color и
    изменить цвет ImageView (iv_bender) на цвет status.color (метод setColorFilter(color,"MULTIPLY"))

    При вводе неверного ответа более 3 раз сбросить состояние сущности Bender на значение по умолчанию (status = Status.NORMAL, question = Question.NAME)
    и вернуть "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color и изменить цвет ImageView (iv_bender) на цвет status.color
    Необходимо сохранять состояние экземпляра класса Bender при пересоздании Activity (достаточно сохранить Status, Question)
     */
    //счетчик ответов
    private var counterWrongAnswer : Int =0

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question.answer.contains(answer)) {
            question = question.nextQUESTION()
            "Отлично - ты справился\n${question.question}" to status.color
        } else {
            counterWrongAnswer++
            if (counterWrongAnswer <= 3) {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }else {
                status = Status.NORMAL
                question = Question.NAME
                counterWrongAnswer = 0
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 255, 0));


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
            override fun nextQUESTION(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQUESTION(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQUESTION(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQUESTION(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQUESTION(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQUESTION(): Question = IDLE
        };

        abstract fun nextQUESTION(): Question
    }

}
