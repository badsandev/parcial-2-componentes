const { Payment, Member } = require('../models');

// Repositorio: capa de acceso a datos para pagos
const PaymentRepository = {

  // Obtener todos los pagos de una meta
  findByGoalId: async (goalId) => {
    return await Payment.findAll({
      where: { goalId },
      include: [{ model: Member, as: 'member', attributes: ['id', 'name'] }],
      order: [['paymentDate', 'DESC']],
    });
  },

  // Obtener todos los pagos de un miembro
  findByMemberId: async (memberId) => {
    return await Payment.findAll({
      where: { memberId },
      order: [['paymentDate', 'DESC']],
    });
  },

  // Registrar un nuevo pago
  create: async (data) => {
    return await Payment.create(data);
  },

  // Eliminar un pago
  delete: async (id) => {
    const payment = await Payment.findByPk(id);
    if (!payment) return null;
    await payment.destroy();
    return true;
  },
};

module.exports = PaymentRepository;
