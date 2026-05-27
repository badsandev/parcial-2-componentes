const sequelize = require('../config/database');
const Goal = require('./Goal');
const Member = require('./Member');
const Payment = require('./Payment');

// Una meta tiene muchos miembros
Goal.hasMany(Member, { foreignKey: 'goalId', as: 'members', onDelete: 'CASCADE' });
Member.belongsTo(Goal, { foreignKey: 'goalId', as: 'goal' });

// Una meta tiene muchos pagos
Goal.hasMany(Payment, { foreignKey: 'goalId', as: 'payments', onDelete: 'CASCADE' });
Payment.belongsTo(Goal, { foreignKey: 'goalId', as: 'goal' });

// Un miembro tiene muchos pagos
Member.hasMany(Payment, { foreignKey: 'memberId', as: 'payments', onDelete: 'CASCADE' });
Payment.belongsTo(Member, { foreignKey: 'memberId', as: 'member' });

module.exports = { sequelize, Goal, Member, Payment };
