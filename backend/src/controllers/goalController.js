const GoalService = require('../services/goalService');

// Controlador: recibe la petición HTTP y delega al servicio
const GoalController = {

  // GET /api/goals
  getAllGoals: async (req, res) => {
    try {
      const goals = await GoalService.getAllGoals();
      res.json({ success: true, data: goals });
    } catch (error) {
      res.status(500).json({ success: false, message: error.message });
    }
  },

  // GET /api/goals/:id
  getGoalById: async (req, res) => {
    try {
      const goal = await GoalService.getGoalById(req.params.id);
      res.json({ success: true, data: goal });
    } catch (error) {
      const status = error.message === 'Meta no encontrada' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // POST /api/goals
  createGoal: async (req, res) => {
    try {
      // Si hay imagen subida, se guarda la ruta relativa
      const imageUrl = req.file ? `/uploads/${req.file.filename}` : null;
      const goalData = { ...req.body, imageUrl };
      const goal = await GoalService.createGoal(goalData);
      res.status(201).json({ success: true, data: goal });
    } catch (error) {
      const status = error.message.includes('obligatorio') ? 400 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // PUT /api/goals/:id
  updateGoal: async (req, res) => {
    try {
      const imageUrl = req.file ? `/uploads/${req.file.filename}` : undefined;
      const updateData = imageUrl ? { ...req.body, imageUrl } : req.body;
      const goal = await GoalService.updateGoal(req.params.id, updateData);
      res.json({ success: true, data: goal });
    } catch (error) {
      const status = error.message === 'Meta no encontrada' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // DELETE /api/goals/:id
  deleteGoal: async (req, res) => {
    try {
      const result = await GoalService.deleteGoal(req.params.id);
      res.json({ success: true, ...result });
    } catch (error) {
      const status = error.message === 'Meta no encontrada' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },
};

module.exports = GoalController;
