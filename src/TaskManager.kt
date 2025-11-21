import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TaskManager {
    private val tasks = mutableListOf<Task>()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val fileName = "tasks.txt"

    init {
        loadFromFile()
    }

    fun addTask(title: String, description: String, category: String, priority: Priority, dueDate: String) {
        val id = if (tasks.isEmpty()) 1 else tasks.maxOf { it.id } + 1
        val createdAt = LocalDate.now().format(dateFormatter)
        val task = Task(id, title, description, category, priority, dueDate, false, createdAt)
        tasks.add(task)
        saveToFile()
    }

    fun deleteTask(id: Int) {
        val removed = tasks.removeIf { it.id == id }
        if (removed) {
            println("Задача с ID $id удалена.")
            saveToFile()
        } else {
            println("Задача с ID $id не найдена.")
        }
    }

    fun markCompleted(id: Int) {
        val task = tasks.find { it.id == id }
        if (task == null) {
            println("Задача с ID $id не найдена.")
        } else if (task.isCompleted) {
            println("Задача уже отмечена как выполненная.")
        } else {
            task.isCompleted = true
            saveToFile()
            println("Задача отмечена как выполненная.")
        }
    }

    fun findTasks(title: String? = null, category: String? = null, priority: Priority? = null) {
        val foundTasks = tasks.filter { task ->
            (title == null || task.title.contains(title, ignoreCase = true)) &&
                    (category == null || task.category.equals(category, ignoreCase = true)) &&
                    (priority == null || task.priority == priority)
        }
        if (foundTasks.isEmpty()) {
            println("Задачи не найдены.")
        } else {
            foundTasks.forEach {
                println("${it.id}. ${it.title} | ${it.category} | ${it.priority} | Срок: ${it.dueDate} | Выполнена: ${it.isCompleted}")
            }
        }
    }

    fun showStatistics() {
        val total = tasks.size
        val completed = tasks.count { it.isCompleted }
        val pending = total - completed
        println("Всего задач: $total")
        println("Выполнено: $completed")
        println("В работе: $pending")
    }

    private fun saveToFile() {
        try {
            File(fileName).printWriter().use { out ->
                tasks.forEach {
                    out.println("${it.id}|${it.title}|${it.description}|${it.category}|${it.priority}|${it.dueDate}|${it.isCompleted}|${it.createdAt}")
                }
            }
        } catch (e: Exception) {
            println("Ошибка записи файла: ${e.message}")
        }
    }

    private fun loadFromFile() {
        try {
            val file = File(fileName)
            if (!file.exists()) return
            file.readLines().forEach { line ->
                val parts = line.split("|")
                if (parts.size == 8) {
                    try {
                        val task = Task(
                            id = parts[0].toInt(),
                            title = parts[1],
                            description = parts[2],
                            category = parts[3],
                            priority = Priority.valueOf(parts[4]),
                            dueDate = parts[5],
                            isCompleted = parts[6].toBoolean(),
                            createdAt = parts[7]
                        )
                        tasks.add(task)
                    } catch (e: Exception) {
                        println("Ошибка загрузки одной из задач: ${e.message}")
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка чтения файла: ${e.message}")
        }
    }

    fun showAllTasks() {
        if (tasks.isEmpty()) {
            println("Задачи отсутствуют.")
            return
        }
        tasks.forEach {
            println("${it.id}. ${it.title} | ${it.description} | ${it.category} | ${it.priority} | Срок: ${it.dueDate} | Выполнена: ${it.isCompleted}")
        }
    }
}