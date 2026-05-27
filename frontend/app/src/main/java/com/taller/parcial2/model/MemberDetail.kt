package com.taller.parcial2.model

// Modelo: miembro con sus aportes (usado en el detalle de la meta)
data class MemberDetail(
    val id: String,
    val name: String,
    val totalContributed: Double,
    val paymentsCount: Int,
    val payments: List<Payment>
)
