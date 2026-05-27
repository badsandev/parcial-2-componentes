const PaymentRepository = require('../repositories/paymentRepository');
const MemberRepository = require('../repositories/memberRepository');
const GoalRepository = require('../repositories/goalRepository');

// Servicio: contiene la lógica de negocio para los pagos
const PaymentService = {

  // Obtener todos los pagos de una meta
  getPaymentsByGoal: async (goalId) => {
    const goal = await GoalRepository.findById(goalId);
    if (!goal) throw new Error('Meta no encontrada');
    return await PaymentRepository.findByGoalId(goalId);
  },

  // Registrar un pago de un miembro hacia una meta
  // Nota: un miembro puede pagar por otro (cualquier integrante)
  registerPayment: async ({ goalId, memberId, amount, note, paymentDate }) => {
    if (!goalId || !memberId || !amount) {
      throw new Error('Meta, miembro y monto son obligatorios');
    }
    if (isNaN(amount) || Number(amount) <= 0) {
      throw new Error('El monto del pago debe ser mayor a 0');
    }

    // Validar que el miembro pertenece a la meta
    const member = await MemberRepository.findById(memberId);
    if (!member) throw new Error('Miembro no encontrado');
    if (member.goalId !== goalId) throw new Error('El miembro no pertenece a esta meta');

    return await PaymentRepository.create({
      goalId,
      memberId,
      amount: Number(amount),
      note: note || null,
      paymentDate: paymentDate || new Date(),
    });
  },

  // Eliminar un pago
  deletePayment: async (id) => {
    const result = await PaymentRepository.delete(id);
    if (!result) throw new Error('Pago no encontrado');
    return { message: 'Pago eliminado correctamente' };
  },
};

module.exports = PaymentService;
