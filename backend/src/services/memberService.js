const MemberRepository = require('../repositories/memberRepository');
const GoalRepository = require('../repositories/goalRepository');

// Servicio: contiene la lógica de negocio para los miembros
const MemberService = {

  // Obtener todos los miembros de una meta
  getMembersByGoal: async (goalId) => {
    const goal = await GoalRepository.findById(goalId);
    if (!goal) throw new Error('Meta no encontrada');
    return await MemberRepository.findByGoalId(goalId);
  },

  // Agregar un miembro a una meta existente
  addMember: async (goalId, name) => {
    if (!name || name.trim() === '') throw new Error('El nombre del miembro es obligatorio');

    const goal = await GoalRepository.findById(goalId);
    if (!goal) throw new Error('Meta no encontrada');

    return await MemberRepository.create({ name: name.trim(), goalId });
  },

  // Eliminar un miembro de una meta
  deleteMember: async (id) => {
    const result = await MemberRepository.delete(id);
    if (!result) throw new Error('Miembro no encontrado');
    return { message: 'Miembro eliminado correctamente' };
  },
};

module.exports = MemberService;
