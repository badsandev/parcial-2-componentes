package com.taller.parcial2.viewmodel

import com.taller.parcial2.model.GoalDetail
import com.taller.parcial2.repository.SavingsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: SavingsRepository
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setup() {
        // Configuramos el despachador de corrutinas para pruebas
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = DetailViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `calculateProgress devuelve el porcentaje correcto`() {
        // Escenario: Meta de 1000, ahorrado 500 -> Debería ser 0.5 (50%)
        val progress = viewModel.calculateProgress(500.0, 1000.0)
        assertEquals(0.5f, progress, 0.01f)
    }

    @Test
    fun `calculateProgress limita el resultado a 1f si se sobrepasa la meta`() {
        // Escenario: Meta de 1000, ahorrado 1500 -> Debería ser 1.0 (100%)
        val progress = viewModel.calculateProgress(1500.0, 1000.0)
        assertEquals(1f, progress)
    }

    @Test
    fun `loadGoal actualiza el estado cuando el repositorio responde con exito`() {
        // Given (Dado un ID y una meta simulada)
        val goalId = "goal_123"
        val mockGoal = GoalDetail(
            id = goalId,
            name = "Viaje",
            description = "Ahorro para vacaciones",
            targetAmount = 5000.0,
            totalSaved = 1000.0,
            remainingAmount = 4000.0,
            progressPercentage = 20.0,
            imageUrl = null,
            members = emptyList(),
            recentPayments = emptyList(),
            createdAt = "2023-10-01"
        )
        
        // Configuramos el Mock para que devuelva éxito
        coEvery { repository.getGoalById(goalId) } returns Result.success(mockGoal)

        // When (Cuando cargamos la meta)
        viewModel.loadGoal(goalId)

        // Then (Entonces el estado de la UI debe coincidir)
        val currentState = viewModel.uiState.value
        assertEquals(mockGoal, currentState.goal)
        assertEquals(false, currentState.isLoading)
        assertEquals(null, currentState.error)
    }
}
