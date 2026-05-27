const { Member, Payment } = require('../models');

// Repositorio: capa de acceso a datos para miembros
const MemberRepository = {

  // Obtener todos los miembros de una meta
  findByGoalId: async (goalId) => {
    return await Member.findAll({
      where: { goalId },
      include: [{ model: Payment, as: 'payments' }],
      order: [['createdAt', 'ASC']],
    });
  },

  // Obtener un miembro por ID
  findById: async (id) => {
    return await Member.findByPk(id, {
      include: [{ model: Payment, as: 'payments' }],
    });
  },

  // Crear un miembro y asociarlo a una meta
  create: async (data) => {
    return await Member.create(data);
  },

  // Eliminar un miembro
  delete: async (id) => {
    const member = await Member.findByPk(id);
    if (!member) return null;
    await member.destroy();
    return true;
  },
};

module.exports = MemberRepository;
