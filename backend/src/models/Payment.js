const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

// Pago: aporte realizado por un miembro hacia la meta
const Payment = sequelize.define('Payment', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  amount: {
    type: DataTypes.DECIMAL(10, 2),
    allowNull: false,
  },
  note: {
    type: DataTypes.STRING,
    allowNull: true,
  },
  paymentDate: {
    type: DataTypes.DATEONLY,
    allowNull: false,
    defaultValue: DataTypes.NOW,
  },
  // memberId y goalId se agregan por las asociaciones
}, {
  tableName: 'payments',
  timestamps: true,
});

module.exports = Payment;
