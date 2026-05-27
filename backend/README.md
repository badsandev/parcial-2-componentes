# 💰 Savings API — Sistema de Ahorro Familiar

API REST para gestionar metas de ahorro familiar con miembros y pagos.

## 🏗️ Arquitectura

```
src/
├── config/         → Conexión a PostgreSQL (Sequelize)
├── models/         → Definición de tablas y asociaciones
├── repositories/   → Acceso a datos (queries SQL via Sequelize)
├── services/       → Lógica de negocio y cálculos
├── controllers/    → Manejo de peticiones HTTP
└── routes/         → Definición de endpoints
```

**Flujo:** `Route → Controller → Service → Repository → DB`

## ⚙️ Configuración

### 1. Instalar dependencias
```bash
npm install
```

### 2. Configurar variables de entorno
```bash
cp .env.example .env
# Editar .env con tus credenciales de PostgreSQL
```

### 3. Crear la base de datos en PostgreSQL
```sql
CREATE DATABASE savings_db;
```

### 4. Iniciar el servidor
```bash
# Desarrollo (con auto-reload)
npm run dev

# Producción
npm start
```

---

## 📋 Endpoints

### Metas de Ahorro

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/goals` | Listar todas las metas con progreso |
| POST | `/api/goals` | Crear una nueva meta |
| GET | `/api/goals/:id` | Detalle completo de una meta |
| PUT | `/api/goals/:id` | Actualizar una meta |
| DELETE | `/api/goals/:id` | Eliminar una meta |

**POST /api/goals** (multipart/form-data)
```
name         → string (obligatorio)
targetAmount → number (obligatorio)
description  → string (opcional)
image        → archivo imagen JPG/PNG/WEBP (opcional)
```

**Respuesta GET /api/goals**
```json
{
  "success": true,
  "data": [
    {
      "id": "uuid",
      "name": "Televisor Samsung",
      "targetAmount": 2000000,
      "totalSaved": 800000,
      "remainingAmount": 1200000,
      "progressPercentage": 40.00,
      "membersCount": 3,
      "imageUrl": "/uploads/1234567890.jpg"
    }
  ]
}
```

---

### Miembros

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/goals/:goalId/members` | Listar miembros de una meta |
| POST | `/api/goals/:goalId/members` | Agregar un miembro |
| DELETE | `/api/goals/:goalId/members/:id` | Eliminar un miembro |

**POST /api/goals/:goalId/members** (JSON)
```json
{ "name": "Juan Pérez" }
```

---

### Pagos

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/goals/:goalId/payments` | Listar pagos de una meta |
| POST | `/api/goals/:goalId/payments` | Registrar un pago |
| DELETE | `/api/goals/:goalId/payments/:id` | Eliminar un pago |

**POST /api/goals/:goalId/payments** (JSON)
```json
{
  "memberId": "uuid-del-miembro",
  "amount": 150000,
  "note": "Aporte de enero",
  "paymentDate": "2025-01-15"
}
```

> ⚠️ Un miembro puede registrar un pago en nombre de otro miembro del mismo grupo.

---

## 🗄️ Modelo de datos

```
Goal (meta)
  ├── id, name, description, targetAmount, imageUrl
  ├── members[]
  │     ├── id, name, goalId
  │     └── payments[]
  └── payments[]
        ├── id, amount, note, paymentDate
        ├── memberId → Member
        └── goalId   → Goal
```
