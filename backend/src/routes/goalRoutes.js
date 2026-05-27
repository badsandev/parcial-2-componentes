const express = require('express');
const router = express.Router();
const GoalController = require('../controllers/goalController');
const upload = require('../middlewares/upload');

// Rutas de metas de ahorro
router.get('/', GoalController.getAllGoals);
router.get('/:id', GoalController.getGoalById);
router.post('/', upload.single('image'), GoalController.createGoal);
router.put('/:id', upload.single('image'), GoalController.updateGoal);
router.delete('/:id', GoalController.deleteGoal);

module.exports = router;
