package com.taller.parcial2.model

// Modelo: resumen de meta para el listado principal
data class GoalSummary(
    val id: String,
    val name: String,
    val description: String?,
    val targetAmount: Double,
    val totalSaved: Double,
    val remainingAmount: Double,
    val progressPercentage: Double,
    val membersCount: Int,
    val imageUrl: String?,
    val createdAt: String
)
