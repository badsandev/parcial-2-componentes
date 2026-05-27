package com.taller.parcial2.ui.theme.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.taller.parcial2.model.GoalSummary
import com.taller.parcial2.ui.theme.AppColors
import com.taller.parcial2.ui.theme.components.ErrorMessage
import com.taller.parcial2.ui.theme.components.LoadingIndicator
import com.taller.parcial2.ui.theme.components.ProgressBar
import com.taller.parcial2.utils.FormatUtils
import com.taller.parcial2.viewmodel.GoalsViewModel

// Pantalla principal: lista de metas de ahorro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    onGoalClick: (String) -> Unit,
    viewModel: GoalsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.successMessage, uiState.error) {
        uiState.successMessage?.let { snackbarHostState.showSnackbar(it) }
        uiState.error?.let { snackbarHostState.showSnackbar(it) }
        viewModel.clearMessages()
    }

    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Buenos días",
                            fontSize = 12.sp,
                            color = AppColors.OnSurfaceSubtle
                        )
                        Text(
                            "Mis metas 💰",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Background,
                    titleContentColor = AppColors.OnBackground
                ),
                actions = {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AppColors.SurfaceVariant)
                            .clickable { showCreateDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Nueva meta",
                            tint = AppColors.OnBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.error != null && uiState.goals.isEmpty() ->
                ErrorMessage(uiState.error!!, onRetry = { viewModel.loadGoals() })
            uiState.goals.isEmpty() -> EmptyGoals(onAdd = { showCreateDialog = true })
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(AppColors.Background),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Meta principal (primera) con card degradado
                    item {
                        val mainGoal = uiState.goals.first()
                        MainGoalCard(
                            goal = mainGoal,
                            onClick = { onGoalClick(mainGoal.id) },
                            onDelete = { viewModel.deleteGoal(mainGoal.id) }
                        )
                    }

                    // Otras metas
                    if (uiState.goals.size > 1) {
                        item {
                            Text(
                                "Otras metas",
                                fontSize = 14.sp,
                                color = AppColors.OnSurfaceSubtle,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                        items(uiState.goals.drop(1)) { goal ->
                            GoalCard(
                                goal = goal,
                                onClick = { onGoalClick(goal.id) },
                                onDelete = { viewModel.deleteGoal(goal.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateGoalDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, amount, description ->
                viewModel.createGoal(name, amount, description)
                showCreateDialog = false
            }
        )
    }
}

// Tarjeta principal con degradado (primera meta)
@Composable
fun MainGoalCard(goal: GoalSummary, onClick: () -> Unit, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(AppColors.GradientStart, AppColors.GradientEnd)
                )
            )
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Meta principal",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            Text(
                text = goal.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            if (!goal.description.isNullOrBlank()) {
                Text(
                    text = goal.description,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Barra de progreso blanca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth((goal.progressPercentage / 100).toFloat().coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    FormatUtils.formatCurrency(goal.totalSaved),
                    fontSize = 13.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "/ ${FormatUtils.formatCurrency(goal.targetAmount)}",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }
    }

    if (showDeleteDialog) {
        DarkAlertDialog(
            title = "¿Eliminar meta?",
            text = "Se eliminará '${goal.name}' con todos sus miembros y pagos.",
            onConfirm = { onDelete(); showDeleteDialog = false },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

// Tarjeta de meta secundaria – fondo oscuro
@Composable
fun GoalCard(goal: GoalSummary, onClick: () -> Unit, onDelete: () -> Unit) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono izquierdo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(AppColors.SurfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Savings,
                    contentDescription = null,
                    tint = AppColors.Primary,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido central
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = goal.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.OnBackground
                )
                if (!goal.description.isNullOrBlank()) {
                    Text(
                        text = goal.description,
                        fontSize = 12.sp,
                        color = AppColors.OnSurfaceSubtle
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBar(
                    progress = (goal.progressPercentage / 100).toFloat(),
                    percentage = goal.progressPercentage
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${FormatUtils.formatCurrency(goal.totalSaved)} / ${FormatUtils.formatCurrency(goal.targetAmount)}",
                    fontSize = 11.sp,
                    color = AppColors.OnSurfaceSubtle
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Porcentaje + eliminar
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${goal.progressPercentage.toInt()}%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = AppColors.Danger,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        DarkAlertDialog(
            title = "¿Eliminar meta?",
            text = "Se eliminará '${goal.name}' con todos sus miembros y pagos.",
            onConfirm = { onDelete(); showDeleteDialog = false },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

// AlertDialog con estilo dark
@Composable
fun DarkAlertDialog(title: String, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppColors.Surface,
        titleContentColor = AppColors.OnBackground,
        textContentColor = AppColors.OnSurfaceSubtle,
        title = { Text(title) },
        text = { Text(text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = AppColors.Danger)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = AppColors.OnSurfaceSubtle)
            }
        }
    )
}

// Diálogo para crear una nueva meta
@Composable
fun CreateGoalDialog(onDismiss: () -> Unit, onCreate: (String, Double, String?) -> Unit) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppColors.Surface,
        titleContentColor = AppColors.OnBackground,
        title = { Text("Nueva meta", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DarkTextField(value = name, onValueChange = { name = it }, label = "Nombre de la meta", placeholder = "Ej. Fondo para Carro")
                DarkTextField(value = description, onValueChange = { description = it }, label = "Descripción")
                DarkTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Valor total",
                    keyboardType = KeyboardType.Number
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: return@Button
                    onCreate(name, amountDouble, description.ifBlank { null })
                },
                enabled = name.isNotBlank() && amount.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Crear meta") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = AppColors.OnSurfaceSubtle)
            }
        }
    )
}

// TextField oscuro reutilizable
@Composable
fun DarkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AppColors.OnSurfaceSubtle, fontSize = 12.sp) },
        placeholder = { Text(placeholder, color = AppColors.OnSurfaceSubtle.copy(alpha = 0.5f)) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AppColors.Primary,
            unfocusedBorderColor = AppColors.SurfaceVariant,
            focusedContainerColor = AppColors.SurfaceVariant,
            unfocusedContainerColor = AppColors.SurfaceVariant,
            focusedTextColor = AppColors.OnBackground,
            unfocusedTextColor = AppColors.OnBackground,
            cursorColor = AppColors.Primary
        )
    )
}

// Pantalla vacía cuando no hay metas
@Composable
fun EmptyGoals(onAdd: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AppColors.SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Savings,
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                tint = AppColors.Primary
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("No tienes metas de ahorro", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = AppColors.OnBackground)
        Spacer(modifier = Modifier.height(8.dp))
        Text("¡Crea tu primera meta!", color = AppColors.OnSurfaceSubtle)
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAdd,
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("+ Crear meta")
        }
    }
}
