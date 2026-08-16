package com.jeff

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ServerTest {

    @BeforeTest
    fun setUp() {
        TaskRepository.reset()
    }

    @Test
    fun `test root endpoint`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, Jeff!", response.bodyAsText())
    }

    @Test
    fun `test GET all tasks`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val response = client.get("/tasks")
        assertEquals(HttpStatusCode.OK, response.status)
        assertTrue(response.bodyAsText().contains("Learn Ktor Basics"))
    }

    @Test
    fun `test GET task by id success and not found`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val successResponse = client.get("/tasks/1")
        assertEquals(HttpStatusCode.OK, successResponse.status)
        assertTrue(successResponse.bodyAsText().contains("Learn Ktor Basics"))

        val notFoundResponse = client.get("/tasks/999")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)

        val badRequestResponse = client.get("/tasks/invalid-id")
        assertEquals(HttpStatusCode.BadRequest, badRequestResponse.status)
    }

    @Test
    fun `test POST new task`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            setBody("""{"content":"Write unit tests","isDone":false}""")
        }
        assertEquals(HttpStatusCode.Created, response.status)
        assertTrue(response.bodyAsText().contains("Write unit tests"))
    }

    @Test
    fun `test PUT task update success and not found`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val updateResponse = client.put("/tasks/1") {
            contentType(ContentType.Application.Json)
            setBody("""{"id":1,"content":"Learn Ktor Basics (Done)","isDone":true}""")
        }
        assertEquals(HttpStatusCode.OK, updateResponse.status)
        assertTrue(updateResponse.bodyAsText().contains("Learn Ktor Basics (Done)"))

        val notFoundResponse = client.put("/tasks/999") {
            contentType(ContentType.Application.Json)
            setBody("""{"id":999,"content":"Non-existent","isDone":false}""")
        }
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)
    }

    @Test
    fun `test DELETE task success and not found`() = testApplication {
        application {
            configureSerialization()
            configureRouting()
        }
        val deleteResponse = client.delete("/tasks/1")
        assertEquals(HttpStatusCode.NoContent, deleteResponse.status)

        val notFoundResponse = client.delete("/tasks/999")
        assertEquals(HttpStatusCode.NotFound, notFoundResponse.status)
    }
}
