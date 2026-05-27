const MemberService = require('../services/memberService');

// Controlador: recibe la petición HTTP y delega al servicio
const MemberController = {

  // GET /api/goals/:goalId/members
  getMembersByGoal: async (req, res) => {
    try {
      const members = await MemberService.getMembersByGoal(req.params.goalId);
      res.json({ success: true, data: members });
    } catch (error) {
      const status = error.message === 'Meta no encontrada' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // POST /api/goals/:goalId/members
  addMember: async (req, res) => {
    try {
      const { name } = req.body;
      const member = await MemberService.addMember(req.params.goalId, name);
      res.status(201).json({ success: true, data: member });
    } catch (error) {
      const status = ['Meta no encontrada', 'obligatorio'].some(m => error.message.includes(m)) ? 400 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },

  // DELETE /api/members/:id
  deleteMember: async (req, res) => {
    try {
      const result = await MemberService.deleteMember(req.params.id);
      res.json({ success: true, ...result });
    } catch (error) {
      const status = error.message === 'Miembro no encontrado' ? 404 : 500;
      res.status(status).json({ success: false, message: error.message });
    }
  },
};

module.exports = MemberController;
