package com.jeff

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello, Jeff!")
        }

        route("/tasks") {
            // GET /tasks: คืนค่า task ทั้งหมดด้วย call.respond() และ status 200 OK
            get {
                call.respond(HttpStatusCode.OK, TaskRepository.getAll())
            }

            // GET /tasks/{id}: ค้นหาและคืนค่า task เพียงตัวเดียว หรือตอบกลับด้วย 404 Not Found หากไม่พบ
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@get
                }

                val task = TaskRepository.getById(id)
                if (task != null) {
                    call.respond(HttpStatusCode.OK, task)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            }

            // POST /tasks: ใช้ call.receive<Task>() เพื่อรับ task ใหม่, เพิ่มลงใน repository, และตอบกลับด้วย 201 Created
            post {
                val task = call.receive<Task>()
                val createdTask = TaskRepository.add(task)
                call.respond(HttpStatusCode.Created, createdTask)
            }

            // PUT /tasks/{id}: รับ ID และข้อมูล task ที่อัปเดตจาก request, อัปเดตข้อมูลใน repository, และตอบกลับด้วย 200 OK
            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@put
                }

                val updatedTask = call.receive<Task>()
                val success = TaskRepository.update(id, updatedTask)
                if (success) {
                    call.respond(HttpStatusCode.OK, updatedTask.copy(id = id))
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            }

            // DELETE /tasks/{id}: รับ ID, ลบ task ออกจาก repository, และตอบกลับด้วย 204 No Content
            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format")
                    return@delete
                }

                val deleted = TaskRepository.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            }
        }
    }
}