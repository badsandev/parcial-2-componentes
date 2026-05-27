package com.taller.parcial2.ui.theme.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.taller.parcial2.model.*
import com.taller.parcial2.ui.theme.AppColors
import com.taller.parcial2.ui.theme.components.AmountCard
import com.taller.parcial2.ui.theme.components.LoadingIndicator
import com.taller.parcial2.ui.theme.goals.DarkAlertDialog
import com.taller.parcial2.ui.theme.goals.DarkTextField
import com.taller.parcial2.utils.FormatUtils
import com.taller.parcial2.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    goalId: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddMember by remember { mutableStateOf(false) }
    var showAddPayment by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(goalId) { viewModel.loadGoal(goalId) }

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
                    Text(
                        uiState.goal?.name ?: "Detalle",
                        fontWeight = FontWeight.Bold,
                        color = AppColors.OnBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = AppColors.OnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Background,
                    titleContentColor = AppColors.OnBackground
                ),
                actions = {
                    IconButton(onClick = { showAddMember = true }) {
                        Icon(
                            Icons.Default.PersonAdd,
                            contentDescription = "Agregar miembro",
                            tint = AppColors.Primary
                        )
                    }
                    IconButton(onClick = { showAddPayment = true }) {
                        Icon(
                            Icons.Default.AddCard,
                            contentDescription = "Registrar pago",
                            tint = AppColors.Primary
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        when {
            uiState.isLoading -> LoadingIndicator()
            uiState.goal == null -> LoadingIndicator()
            else -> {
                val goal = uiState.goal!!
                val progress = viewModel.calculateProgress(goal.totalSaved, goal.targetAmount)

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(AppColors.Background),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(AppColors.GradientStart, AppColors.GradientEnd)
                                    )
                                )
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    FormatUtils.formatCurrency(goal.totalSaved),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "de ${FormatUtils.formatCurrency(goal.targetAmount)}",
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(Color.White.copy(alpha = 0.3f))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(progress.coerceIn(0f, 1f))
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
                                        "${goal.progressPercentage.toInt()}% completado",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        "Faltan ${FormatUtils.formatCurrency(goal.remainingAmount)}",
                                        fontSize = 12.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                AmountCard("Aportado", goal.totalSaved, AppColors.Success)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                AmountCard("Por miembro", if (goal.members.isNotEmpty()) goal.totalSaved / goal.members.size else 0.0, AppColors.Accent)
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                AmountCard("Miembros", goal.members.size.toDouble(), AppColors.Primary)
                            }
                        }
                    }

                    item {
                        Text(
                            "Miembros y aportes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.OnBackground
                        )
                    }
                    if (goal.members.isEmpty()) {
                        item {
                            Text(
                                "No hay miembros. Agrega uno con el ícono +",
                                color = AppColors.OnSurfaceSubtle,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        items(goal.members) { member ->
                            MemberCard(
                                member = member,
                                onDelete = { viewModel.deleteMember(goalId, member.id) }
                            )
                        }
                    }

                    item {
                        Text(
                            "Pagos recientes",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.OnBackground
                        )
                    }
                    if (goal.recentPayments.isEmpty()) {
                        item {
                            Text(
                                "No hay pagos registrados aún.",
                                color = AppColors.OnSurfaceSubtle,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        items(goal.recentPayments) { payment ->
                            PaymentCard(
                                payment = payment,
                                onDelete = { viewModel.deletePayment(goalId, payment.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddMember) {
        AddMemberDialog(
            onDismiss = { showAddMember = false },
            onAdd = { name ->
                viewModel.addMember(goalId, name)
                showAddMember = false
            }
        )
    }

    if (showAddPayment) {
        val members = uiState.goal?.members ?: emptyList()
        RegisterPaymentDialog(
            members = members,
            onDismiss = { showAddPayment = false },
            onRegister = { memberId, amount, note ->
                viewModel.registerPayment(goalId, memberId, amount, note, FormatUtils.today())
                showAddPayment = false
            }
        )
    }
}

@Composable
fun MemberAvatar(name: String, color: Color) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

val memberColors = listOf(
    Color(0xFF7C4DFF), Color(0xFF00BFA5), Color(0xFF00B0FF),
    Color(0xFFFF6D00), Color(0xFFE040FB)
)

@Composable
fun MemberCard(member: MemberDetail, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }
    val color = memberColors[(member.name.hashCode() and 0x7FFFFFFF) % memberColors.size]

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MemberAvatar(name = member.name, color = color)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(member.name, fontWeight = FontWeight.SemiBold, color = AppColors.OnBackground)
                    Text(
                        "${member.paymentsCount} pago${if (member.paymentsCount != 1) "s" else ""}",
                        fontSize = 12.sp,
                        color = AppColors.OnSurfaceSubtle
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    FormatUtils.formatCurrency(member.totalContributed),
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Success,
                    fontSize = 14.sp
                )
                IconButton(
                    onClick = { showDelete = true },
                    modifier = Modifier.size(28.dp)
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

    if (showDelete) {
        DarkAlertDialog(
            title = "¿Eliminar miembro?",
            text = "Se eliminará '${member.name}' y todos sus pagos.",
            onConfirm = { onDelete(); showDelete = false },
            onDismiss = { showDelete = false }
        )
    }
}

@Composable
fun PaymentCard(payment: Payment, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AppColors.Success.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = AppColors.Success,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        FormatUtils.formatCurrency(payment.amount),
                        fontWeight = FontWeight.Bold,
                        color = AppColors.Success
                    )
                    Text(
                        payment.memberName ?: "Miembro",
                        fontSize = 12.sp,
                        color = AppColors.OnSurfaceSubtle
                    )
                    if (!payment.note.isNullOrBlank()) {
                        Text(payment.note, fontSize = 11.sp, color = AppColors.OnSurfaceSubtle)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    FormatUtils.formatDate(payment.paymentDate),
                    fontSize = 11.sp,
                    color = AppColors.OnSurfaceSubtle
                )
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
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
}

@Composable
fun AddMemberDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    var name by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppColors.Surface,
        titleContentColor = AppColors.OnBackground,
        title = { Text("Agregar miembro", fontWeight = FontWeight.Bold) },
        text = {
            DarkTextField(value = name, onValueChange = { name = it }, label = "Nombre del miembro")
        },
        confirmButton = {
            Button(
                onClick = { onAdd(name) },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = AppColors.OnSurfaceSubtle) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPaymentDialog(
    members: List<MemberDetail>,
    onDismiss: () -> Unit,
    onRegister: (String, Double, String?) -> Unit
) {
    var selectedMemberId by remember { mutableStateOf(members.firstOrNull()?.id ?: "") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val selectedMemberName = members.find { it.id == selectedMemberId }?.name ?: "Seleccionar"

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppColors.Surface,
        titleContentColor = AppColors.OnBackground,
        title = { Text("Realizar aporte", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = selectedMemberName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Miembro", color = AppColors.OnSurfaceSubtle, fontSize = 12.sp) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AppColors.Primary,
                            unfocusedBorderColor = AppColors.SurfaceVariant,
                            focusedContainerColor = AppColors.SurfaceVariant,
                            unfocusedContainerColor = AppColors.SurfaceVariant,
                            focusedTextColor = AppColors.OnBackground,
                            unfocusedTextColor = AppColors.OnBackground
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.background(AppColors.SurfaceVariant)
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name, color = AppColors.OnBackground) },
                                onClick = { selectedMemberId = member.id; expanded = false }
                            )
                        }
                    }
                }
                DarkTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = "Monto",
                    keyboardType = KeyboardType.Number
                )
                DarkTextField(value = note, onValueChange = { note = it }, label = "Método de pago (opcional)", placeholder = "Transferencia bancaria")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: return@Button
                    onRegister(selectedMemberId, amountDouble, note.ifBlank { null })
                },
                enabled = selectedMemberId.isNotBlank() && amount.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Confirmar aporte") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = AppColors.OnSurfaceSubtle) }
        }
    )
}
