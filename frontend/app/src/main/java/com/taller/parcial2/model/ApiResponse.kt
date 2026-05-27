package com.taller.parcial2.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String?
)
