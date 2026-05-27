const express = require('express');
const router = express.Router({ mergeParams: true }); // mergeParams para acceder a :goalId del padre
const PaymentController = require('../controllers/paymentController');

// Rutas de pagos (anidadas bajo /goals/:goalId/payments)
router.get('/', PaymentController.getPaymentsByGoal);
router.post('/', PaymentController.registerPayment);
router.delete('/:id', PaymentController.deletePayment);

module.exports = router;
