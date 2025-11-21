import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale.getDefault

fun readPriority(): Priority? {
    return try {
        val input = readLine()?.uppercase(getDefault()) ?: return null
        Priority.valueOf(input)
    } catch (e: IllegalArgumentException) {
        println("Ошибка: неверный приоритет. Используйте HIGH, MEDIUM или LOW.")
        null
    }
}

fun readDate(): String? {
    return try {
        val input = readLine() ?: return null
        LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        input
    } catch (e: DateTimeParseException) {
        println("Ошибка: неверный формат даты. Используйте yyyy-MM-dd.")
        null
    }
}

fun readInt(): Int? {
    return try {
        readLine()?.toInt()
    } catch (e: NumberFormatException) {
        println("Ошибка: введено нечисловое значение.")
        null
    }
}

fun main() {
    val taskManager = TaskManager()
    while (true) {
        println(
            """
            === TO-DO LIST ===
            1. Показать все задачи
            2. Добавить задачу
            3. Найти задачу
            4. Отметить как выполненную
            5. Удалить задачу
            6. Статистика
            0. Выход
        """.trimIndent()
        )
        when (readLine()?.trim()) {
            "1" -> taskManager.showAllTasks()
            "2" -> {
                println("Введите название:")
                val title = readLine() ?: ""
                println("Введите описание:")
                val description = readLine() ?: ""
                println("Введите категорию:")
                val category = readLine() ?: ""

                var priority: Priority?
                do {
                    println("Приоритет (HIGH, MEDIUM, LOW):")
                    priority = readPriority()
                } while (priority == null)

                var dueDate: String?
                do {
                    println("Срок выполнения (yyyy-MM-dd):")
                    dueDate = readDate()
                } while (dueDate == null)

                taskManager.addTask(title, description, category, priority, dueDate)
                println("Задача добавлена.")
            }
            "3" -> {
                println("Поиск по названию (или пусто):")
                val title = readLine()?.takeIf { it.isNotBlank() }
                println("Поиск по категории (или пусто):")
                val category = readLine()?.takeIf { it.isNotBlank() }
                println("Поиск по приоритету (HIGH, MEDIUM, LOW или пусто):")
                val pr = readLine()
                val priority = when (pr?.uppercase(getDefault())) {
                    "HIGH" -> Priority.HIGH
                    "MEDIUM" -> Priority.MEDIUM
                    "LOW" -> Priority.LOW
                    else -> null
                }
                taskManager.findTasks(title, category, priority)
            }
            "4" -> {
                println("Введите ID задачи для отметки выполненной:")
                val id = readInt()
                if (id != null) taskManager.markCompleted(id)
            }
            "5" -> {
                println("Введите ID задачи для удаления:")
                val id = readInt()
                if (id != null) taskManager.deleteTask(id)
            }
            "6" -> taskManager.showStatistics()
            "0" -> {
                println("Выход из программы.")
                break
            }
            else -> println("Некорректный ввод")
        }
    }
}