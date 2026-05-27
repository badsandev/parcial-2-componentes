const express = require('express');
const router = express.Router({ mergeParams: true }); // mergeParams para acceder a :goalId del padre
const MemberController = require('../controllers/memberController');

// Rutas de miembros (anidadas bajo /goals/:goalId/members)
router.get('/', MemberController.getMembersByGoal);
router.post('/', MemberController.addMember);
router.delete('/:id', MemberController.deleteMember);

module.exports = router;
