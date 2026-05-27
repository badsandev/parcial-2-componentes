package com.taller.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taller.parcial2.repository.SavingsRepository
import com.taller.parcial2.model.GoalSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado de la pantalla de metas
data class GoalsUiState(
    val goals: List<GoalSummary> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

// ViewModel: gestiona el estado de la lista de metas
// La UI solo observa el StateFlow, nunca llama al repositorio directamente
class GoalsViewModel : ViewModel() {

    private val repository = SavingsRepository()

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState

    init {
        loadGoals()
    }

    fun loadGoals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getAllGoals()
                .onSuccess { goals ->
                    _uiState.value = _uiState.value.copy(goals = goals, isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error desconocido"
                    )
                }
        }
    }

    fun createGoal(name: String, targetAmount: Double, description: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.createGoal(name, targetAmount, description)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Meta creada correctamente")
                    loadGoals()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al crear meta"
                    )
                }
        }
    }

    fun deleteGoal(id: String) {
        viewModelScope.launch {
            repository.deleteGoal(id)
                .onSuccess { loadGoals() }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
