/**
 * dishBelongsToUserOrAdmin
 *
 * @module      :: Policy
 * @description :: Simple policy to allow the authenticated user to access his restaurant pages or an admin
 * @docs        :: http://sailsjs.org/#!documentation/policies
 *
 */
module.exports = function(req, res, next) {

  // User is allowed, proceed to the next policy, 
  // or if this is the last policy, the controller
  if (req.session.user) {
  	Dish.findOne(req.param('id')).done(function (err, dish) {
  		if (err) return res.serverError(err);
  		if (!dish) return res.notFound();

		Restaurant.findOne(dish.restaurant_id).done(function (err, restaurant) {
			if (err) return res.serverError(err);
			if (!restaurant) return res.notFound();

			if (restaurant.user_id == req.session.user)
				return next();

			return user.isAdmin() ? next() : res.forbidden('Vous ne pouvez pas accéder à cette page.');
		});
	});
  } else return res.redirect('/login');
};
