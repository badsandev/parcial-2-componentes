const { Goal, Member, Payment } = require('../models');

// Repositorio: capa de acceso a datos para metas de ahorro
const GoalRepository = {

  // Obtener todas las metas con total ahorrado
  findAll: async () => {
    return await Goal.findAll({
      include: [
        { model: Member, as: 'members' },
        { model: Payment, as: 'payments', attributes: ['amount'] },
      ],
      order: [['createdAt', 'DESC']],
    });
  },

  // Obtener una meta por ID con todos sus detalles
  findById: async (id) => {
    return await Goal.findByPk(id, {
      include: [
        {
          model: Member,
          as: 'members',
          include: [
            { model: Payment, as: 'payments' },
          ],
        },
        { model: Payment, as: 'payments', include: [{ model: Member, as: 'member' }] },
      ],
    });
  },

  // Crear una nueva meta
  create: async (data) => {
    return await Goal.create(data);
  },

  // Actualizar una meta existente
  update: async (id, data) => {
    const goal = await Goal.findByPk(id);
    if (!goal) return null;
    return await goal.update(data);
  },

  // Eliminar una meta
  delete: async (id) => {
    const goal = await Goal.findByPk(id);
    if (!goal) return null;
    await goal.destroy();
    return true;
  },
};

module.exports = GoalRepository;
