package com.taller.parcial2.repository

import  com.taller.parcial2.model.*

import com.taller.parcial2.network.*

// Repositorio: intermediario entre Retrofit y los ViewModels
// Los ViewModels nunca llaman a Retrofit directamente
class SavingsRepository {

    private val api = RetrofitInstance.api

    // ── Metas ──────────────────────────────────────────────

    suspend fun getAllGoals(): Result<List<GoalSummary>> = runCatching {
        val response = api.getAllGoals()
        response.body()?.data ?: error("Error al obtener metas")
    }

    suspend fun getGoalById(id: String): Result<GoalDetail> = runCatching {
        val response = api.getGoalById(id)
        response.body()?.data ?: error("Meta no encontrada")
    }

    suspend fun createGoal(name: String, targetAmount: Double, description: String?): Result<Unit> = runCatching {
        val response = api.createGoal(CreateGoalRequest(name, targetAmount, description))
        if (!response.isSuccessful) error(response.body()?.message ?: "Error al crear meta")
    }

    suspend fun deleteGoal(id: String): Result<Unit> = runCatching {
        val response = api.deleteGoal(id)
        if (!response.isSuccessful) error("Error al eliminar meta")
    }

    // ── Miembros ───────────────────────────────────────────

    suspend fun getMembersByGoal(goalId: String): Result<List<Member>> = runCatching {
        val response = api.getMembersByGoal(goalId)
        response.body()?.data ?: error("Error al obtener miembros")
    }

    suspend fun addMember(goalId: String, name: String): Result<Member> = runCatching {
        val response = api.addMember(goalId, AddMemberRequest(name))
        response.body()?.data ?: error("Error al agregar miembro")
    }

    suspend fun deleteMember(goalId: String, memberId: String): Result<Unit> = runCatching {
        val response = api.deleteMember(goalId, memberId)
        if (!response.isSuccessful) error("Error al eliminar miembro")
    }

    // ── Pagos ──────────────────────────────────────────────

    suspend fun getPaymentsByGoal(goalId: String): Result<List<Payment>> = runCatching {
        val response = api.getPaymentsByGoal(goalId)
        response.body()?.data ?: error("Error al obtener pagos")
    }

    suspend fun registerPayment(
        goalId: String,
        memberId: String,
        amount: Double,
        note: String?,
        paymentDate: String?
    ): Result<Payment> = runCatching {
        val response = api.registerPayment(
            goalId,
            RegisterPaymentRequest(memberId, amount, note, paymentDate)
        )
        response.body()?.data ?: error("Error al registrar pago")
    }

    suspend fun deletePayment(goalId: String, paymentId: String): Result<Unit> = runCatching {
        val response = api.deletePayment(goalId, paymentId)
        if (!response.isSuccessful) error("Error al eliminar pago")
    }
}
