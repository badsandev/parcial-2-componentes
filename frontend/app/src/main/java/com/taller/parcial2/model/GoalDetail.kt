package com.taller.parcial2.model

// Modelo: detalle completo de una meta con miembros y pagos
data class GoalDetail(
    val id: String,
    val name: String,
    val description: String?,
    val targetAmount: Double,
    val totalSaved: Double,
    val remainingAmount: Double,
    val progressPercentage: Double,
    val imageUrl: String?,
    val members: List<MemberDetail>,
    val recentPayments: List<Payment>,
    val createdAt: String
)
