const GoalRepository = require('../repositories/goalRepository');

// Servicio: contiene la lógica de negocio para las metas de ahorro
const GoalService = {

  // Obtener todas las metas con su progreso calculado
  getAllGoals: async () => {
    const goals = await GoalRepository.findAll();
    return goals.map(goal => formatGoalSummary(goal));
  },

  // Obtener detalle completo de una meta
  getGoalById: async (id) => {
    const goal = await GoalRepository.findById(id);
    if (!goal) throw new Error('Meta no encontrada');
    return formatGoalDetail(goal);
  },

  // Crear una nueva meta de ahorro
  createGoal: async (data) => {
    const { name, targetAmount } = data;
    if (!name || !targetAmount) throw new Error('Nombre y monto objetivo son obligatorios');
    if (isNaN(targetAmount) || Number(targetAmount) <= 0) throw new Error('El monto objetivo debe ser mayor a 0');
    return await GoalRepository.create(data);
  },

  // Actualizar una meta existente
  updateGoal: async (id, data) => {
    const goal = await GoalRepository.update(id, data);
    if (!goal) throw new Error('Meta no encontrada');
    return goal;
  },

  // Eliminar una meta
  deleteGoal: async (id) => {
    const result = await GoalRepository.delete(id);
    if (!result) throw new Error('Meta no encontrada');
    return { message: 'Meta eliminada correctamente' };
  },
};

// Formatea el resumen de una meta (para listado)
function formatGoalSummary(goal) {
  const totalSaved = goal.payments.reduce((sum, p) => sum + Number(p.amount), 0);
  const targetAmount = Number(goal.targetAmount);
  const percentage = targetAmount > 0 ? Math.min((totalSaved / targetAmount) * 100, 100) : 0;

  return {
    id: goal.id,
    name: goal.name,
    description: goal.description,
    targetAmount,
    totalSaved: parseFloat(totalSaved.toFixed(2)),
    remainingAmount: parseFloat(Math.max(targetAmount - totalSaved, 0).toFixed(2)),
    progressPercentage: parseFloat(percentage.toFixed(2)),
    membersCount: goal.members.length,
    imageUrl: goal.imageUrl,
    createdAt: goal.createdAt,
  };
}

// Formatea el detalle completo de una meta (para pantalla de detalle)
function formatGoalDetail(goal) {
  const totalSaved = goal.payments.reduce((sum, p) => sum + Number(p.amount), 0);
  const targetAmount = Number(goal.targetAmount);
  const percentage = targetAmount > 0 ? Math.min((totalSaved / targetAmount) * 100, 100) : 0;

  // Calcula el aporte de cada miembro
  const membersWithContributions = goal.members.map(member => {
    const memberTotal = member.payments.reduce((sum, p) => sum + Number(p.amount), 0);
    return {
      id: member.id,
      name: member.name,
      totalContributed: parseFloat(memberTotal.toFixed(2)),
      paymentsCount: member.payments.length,
      payments: member.payments,
    };
  });

  return {
    id: goal.id,
    name: goal.name,
    description: goal.description,
    targetAmount,
    totalSaved: parseFloat(totalSaved.toFixed(2)),
    remainingAmount: parseFloat(Math.max(targetAmount - totalSaved, 0).toFixed(2)),
    progressPercentage: parseFloat(percentage.toFixed(2)),
    imageUrl: goal.imageUrl,
    members: membersWithContributions,
    recentPayments: goal.payments.slice(0, 10), // últimos 10 pagos
    createdAt: goal.createdAt,
  };
}

module.exports = GoalService;
