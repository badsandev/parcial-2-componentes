package com.taller.parcial2.network

import com.taller.parcial2.model.*
import retrofit2.Response
import retrofit2.http.*

// Define todos los endpoints del backend
interface SavingsApiService {

    // ── Metas ──────────────────────────────────────────────
    @GET("api/goals")
    suspend fun getAllGoals(): Response<ApiResponse<List<GoalSummary>>>

    @GET("api/goals/{id}")
    suspend fun getGoalById(@Path("id") id: String): Response<ApiResponse<GoalDetail>>

    @POST("api/goals")
    suspend fun createGoal(@Body body: CreateGoalRequest): Response<ApiResponse<Any>>

    @DELETE("api/goals/{id}")
    suspend fun deleteGoal(@Path("id") id: String): Response<ApiResponse<Any>>

    // ── Miembros ───────────────────────────────────────────
    @GET("api/goals/{goalId}/members")
    suspend fun getMembersByGoal(@Path("goalId") goalId: String): Response<ApiResponse<List<Member>>>

    @POST("api/goals/{goalId}/members")
    suspend fun addMember(
        @Path("goalId") goalId: String,
        @Body body: AddMemberRequest
    ): Response<ApiResponse<Member>>

    @DELETE("api/goals/{goalId}/members/{id}")
    suspend fun deleteMember(
        @Path("goalId") goalId: String,
        @Path("id") id: String
    ): Response<ApiResponse<Any>>

    // ── Pagos ──────────────────────────────────────────────
    @GET("api/goals/{goalId}/payments")
    suspend fun getPaymentsByGoal(@Path("goalId") goalId: String): Response<ApiResponse<List<Payment>>>

    @POST("api/goals/{goalId}/payments")
    suspend fun registerPayment(
        @Path("goalId") goalId: String,
        @Body body: RegisterPaymentRequest
    ): Response<ApiResponse<Payment>>

    @DELETE("api/goals/{goalId}/payments/{id}")
    suspend fun deletePayment(
        @Path("goalId") goalId: String,
        @Path("id") id: String
    ): Response<ApiResponse<Any>>
}
