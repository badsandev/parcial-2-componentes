require('dotenv').config();
const express = require('express');
const cors = require('cors');
const path = require('path');
const { sequelize } = require('./models');

// Rutas
const goalRoutes = require('./routes/goalRoutes');
const memberRoutes = require('./routes/memberRoutes');
const paymentRoutes = require('./routes/paymentRoutes');

const app = express();
const PORT = process.env.PORT || 3000;

// ─── Middlewares globales ────────────────────────────────────────────────────
app.use(cors());                          // Permite peticiones desde la app Android
app.use(express.json());                  // Parseo de body JSON
app.use(express.urlencoded({ extended: true }));

// Servir imágenes estáticamente desde la carpeta /uploads
app.use('/uploads', express.static(path.join(__dirname, '..', 'uploads')));

// ─── Rutas de la API ─────────────────────────────────────────────────────────
app.use('/api/goals', goalRoutes);
app.use('/api/goals/:goalId/members', memberRoutes);
app.use('/api/goals/:goalId/payments', paymentRoutes);

// Ruta de health check
app.get('/health', (req, res) => {
  res.json({ status: 'OK', message: 'API de ahorro familiar funcionando' });
});

// Manejo de rutas no encontradas
app.use((req, res) => {
  res.status(404).json({ success: false, message: 'Ruta no encontrada' });
});

// ─── Conexión a BD y arranque ─────────────────────────────────────────────────
const startServer = async () => {
  try {
    await sequelize.authenticate();
    console.log('✅ Conexión a PostgreSQL establecida');

    // Sincroniza modelos con la BD (crea tablas si no existen)
    await sequelize.sync({ alter: true });
    console.log('✅ Tablas sincronizadas');

    // Crear carpeta uploads si no existe
    const fs = require('fs');
    const uploadsDir = path.join(__dirname, '..', 'uploads');
    if (!fs.existsSync(uploadsDir)) fs.mkdirSync(uploadsDir);

    app.listen(PORT, () => {
      console.log(`🚀 Servidor corriendo en http://localhost:${PORT}`);
      console.log(`📋 Endpoints disponibles:`);
      console.log(`   GET    /api/goals`);
      console.log(`   POST   /api/goals`);
      console.log(`   GET    /api/goals/:id`);
      console.log(`   PUT    /api/goals/:id`);
      console.log(`   DELETE /api/goals/:id`);
      console.log(`   GET    /api/goals/:goalId/members`);
      console.log(`   POST   /api/goals/:goalId/members`);
      console.log(`   DELETE /api/goals/:goalId/members/:id`);
      console.log(`   GET    /api/goals/:goalId/payments`);
      console.log(`   POST   /api/goals/:goalId/payments`);
      console.log(`   DELETE /api/goals/:goalId/payments/:id`);
    });
  } catch (error) {
    console.error('❌ Error al iniciar el servidor:', error.message);
    process.exit(1);
  }
};

startServer();
