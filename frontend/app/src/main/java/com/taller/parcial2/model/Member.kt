package com.taller.parcial2.model

// Modelo: miembro simple (usado en respuestas de crear/listar miembros)
data class Member(
    val id: String,
    val name: String,
    val goalId: String,
    val createdAt: String?
)
