package com.jeff

object TaskRepository {
    private val tasks = mutableListOf<Task>(
        Task(id = 1, content = "Learn Ktor Basics", isDone = true),
        Task(id = 2, content = "Implement Task CRUD API", isDone = false)
    )

    fun getAll(): List<Task> = tasks.toList()

    fun getById(id: Int): Task? = tasks.find { it.id == id }

    fun add(task: Task): Task {
        val nextId = if (task.id > 0 && tasks.none { it.id == task.id }) {
            task.id
        } else {
            (tasks.maxOfOrNull { it.id } ?: 0) + 1
        }
        val newTask = task.copy(id = nextId)
        tasks.add(newTask)
        return newTask
    }

    fun update(id: Int, updatedTask: Task): Boolean {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks[index] = updatedTask.copy(id = id)
            return true
        }
        return false
    }

    fun delete(id: Int): Boolean {
        return tasks.removeIf { it.id == id }
    }

    fun reset() {
        tasks.clear()
        tasks.addAll(
            listOf(
                Task(id = 1, content = "Learn Ktor Basics", isDone = true),
                Task(id = 2, content = "Implement Task CRUD API", isDone = false)
            )
        )
    }
}
