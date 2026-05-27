const PaymentService = require('../services/paymentService');

// Controlador: recibe la petición HTTP y delega al servicio
const PaymentController = {

  // GET /api/goals/:goalId/payments
  getPaymentsByGoal: async (req, res) => {
    try {
      const payments = await PaymentService.getPaymentsByGoal(req.params.goalId);
      res.json({ success: true, data: payments });
    } catch (error) {
      const status = error.message === 'Meta no encontrada' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // POST /api/goals/:goalId/payments
  registerPayment: async (req, res) => {
    try {
      const { memberId, amount, note, paymentDate } = req.body;
      const payment = await PaymentService.registerPayment({
        goalId: req.params.goalId,
        memberId,
        amount,
        note,
        paymentDate,
      });
      res.status(201).json({ success: true, data: payment });
    } catch (error) {
      const status = ['obligatorio', 'no encontrado', 'no pertenece', 'mayor a 0']
        .some(m => error.message.includes(m)) ? 400 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // DELETE /api/payments/:id
  deletePayment: async (req, res) => {
    try {
      const result = await PaymentService.deletePayment(req.params.id);
      res.json({ success: true, ...result });
    } catch (error) {
      const status = error.message === 'Pago no encontrado' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },
};

module.exports = PaymentController;
