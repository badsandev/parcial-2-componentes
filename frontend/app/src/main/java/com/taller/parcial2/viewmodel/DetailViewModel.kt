package com.taller.parcial2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taller.parcial2.repository.SavingsRepository
import com.taller.parcial2.model.GoalDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado de la pantalla de detalle
data class DetailUiState(
    val goal: GoalDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

// ViewModel: gestiona el detalle de una meta, sus miembros y pagos
class DetailViewModel(
    private val repository: SavingsRepository = SavingsRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState

    fun loadGoal(goalId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getGoalById(goalId)
                .onSuccess { goal ->
                    _uiState.value = _uiState.value.copy(goal = goal, isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Error al cargar la meta"
                    )
                }
        }
    }

    fun addMember(goalId: String, name: String) {
        viewModelScope.launch {
            repository.addMember(goalId, name)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Miembro agregado")
                    loadGoal(goalId)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun deleteMember(goalId: String, memberId: String) {
        viewModelScope.launch {
            repository.deleteMember(goalId, memberId)
                .onSuccess { loadGoal(goalId) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun registerPayment(goalId: String, memberId: String, amount: Double, note: String?, date: String?) {
        viewModelScope.launch {
            repository.registerPayment(goalId, memberId, amount, note, date)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(successMessage = "Pago registrado")
                    loadGoal(goalId)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    fun deletePayment(goalId: String, paymentId: String) {
        viewModelScope.launch {
            repository.deletePayment(goalId, paymentId)
                .onSuccess { loadGoal(goalId) }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message)
                }
        }
    }

    // Calcula el progreso de la meta — lógica en el ViewModel, no en la UI
    fun calculateProgress(totalSaved: Double, targetAmount: Double): Float {
        if (targetAmount <= 0) return 0f
        return (totalSaved / targetAmount).toFloat().coerceIn(0f, 1f)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, successMessage = null)
    }
}
