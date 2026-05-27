package com.taller.parcial2.model

// Request body para crear una meta
data class CreateGoalRequest(
    val name: String,
    val targetAmount: Double,
    val description: String? = null
)

// Request body para agregar un miembro
data class AddMemberRequest(
    val name: String
)

// Request body para registrar un pago
data class RegisterPaymentRequest(
    val memberId: String,
    val amount: Double,
    val note: String? = null,
    val paymentDate: String? = null
)
