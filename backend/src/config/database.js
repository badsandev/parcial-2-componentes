const { Sequelize } = require('sequelize');
require('dotenv').config();

const sequelize = new Sequelize(
  'savings_db',
  'root',
  '123456',
  {
    host: '127.0.0.1',
    port: 3306,
    dialect: 'mysql',
    logging: false,
  }
);

module.exports = sequelize;