package com.jeff

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int = 0,
    val content: String,
    val isDone: Boolean = false
)

@Serializable
data class TaskRequest(
    val content: String,
    val isDone: Boolean = false
)
