const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

// Miembro: integrante que participa en una meta de ahorro
const Member = sequelize.define('Member', {
  id: {
    type: DataTypes.UUID,
    defaultValue: DataTypes.UUIDV4,
    primaryKey: true,
  },
  name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  // goalId se agrega automáticamente por la asociación en index.js
}, {
  tableName: 'members',
  timestamps: true,
});

module.exports = Member;
