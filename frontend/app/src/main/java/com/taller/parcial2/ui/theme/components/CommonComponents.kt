package com.taller.parcial2.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taller.parcial2.ui.theme.AppColors
import com.taller.parcial2.utils.FormatUtils

// Barra de progreso con porcentaje – estilo dark
@Composable
fun ProgressBar(
    progress: Float,
    percentage: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${FormatUtils.formatPercentage(percentage)} completado",
                fontSize = 12.sp,
                color = AppColors.OnSurfaceSubtle
            )
            if (percentage < 100) {
                Text(
                    text = "Faltan ${FormatUtils.formatPercentage(100 - percentage)}",
                    fontSize = 12.sp,
                    color = AppColors.Danger
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(50))
                .background(AppColors.ProgressTrack)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (percentage >= 100)
                            Brush.horizontalGradient(listOf(AppColors.Success, AppColors.Success))
                        else
                            Brush.horizontalGradient(
                                listOf(AppColors.GradientStart, AppColors.GradientEnd)
                            )
                    )
            )
        }
    }
}

// Tarjeta de resumen de monto – estilo dark
@Composable
fun AmountCard(label: String, amount: Double, color: Color = AppColors.Primary) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AppColors.SurfaceVariant),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 11.sp, color = AppColors.OnSurfaceSubtle)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = FormatUtils.formatCurrency(amount),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

// Indicador de carga centrado
@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = AppColors.Primary)
    }
}

// Mensaje de error
@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "❌ $message", color = AppColors.Danger)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = AppColors.Primary)
        ) { Text("Reintentar") }
    }
}
