/**
 * isManager
 *
 * @module      :: Policy
 * @description :: Simple policy to allow the managers to access some pages
 * @docs        :: http://sailsjs.org/#!documentation/policies
 *
 */

module.exports = function(req, res, next) {

  // User is allowed, proceed to the next policy, 
  // or if this is the last policy, the controller
  if (req.session.user) {
	User.findOne(req.session.user).done(function (err, user) {
		if (err) return res.serverError(err);
		if (!user) { 
			req.session.user = null;
			return res.redirect('/login');
		}

		return user.isManager() ? next() : res.forbidden('Vous ne pouvez pas accéder à cette page.');
	})
  } else return res.redirect('/login');
};
