package com.taller.parcial2.model

// Modelo: pago realizado por un miembro
data class Payment(
    val id: String,
    val amount: Double,
    val note: String?,
    val paymentDate: String,
    val memberId: String,
    val memberName: String?,
    val goalId: String,
    val createdAt: String?
)
