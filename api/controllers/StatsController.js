/**
 * StatsController
 *
 * @description :: Server-side logic for managing Stats
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

var Promise = require('q');

module.exports = {
	
	index : function(req, res) {
		
		var date = new Date(sails.moment().format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z');
		
		Promise.all([
			Booking.count({restaurant_id: req.session.user.restaurant, where: {start : {'<=': date}}}),
		])
		.spread(function () {
			
		})
		.catch(function() {
			res.serverError("Request fail");
		});
	},
	
	getStats : function(req, res) {
		Booking.count({restaurant_id: req.session.user.restaurant}).exec(function (err, booking){
			if (err)
				res.serverError(err);		
			return res.view('stats/index');
		});
	}
};

