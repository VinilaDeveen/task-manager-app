package com.example.taskmanager

data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val isCompleted: Boolean
)
