/**
* StatsController
*
* @description :: Server-side logic for managing Stats
* @help        :: See http://links.sailsjs.org/docs/controllers
*/

var Promise = require('q');

module.exports = {
	
	index : function(req, res) {
		
		var months = [];
		
		for (var i = 0; i < 12; i++) {
			months.push(sails.moment().date(1).month(i).hour(1).format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z');
		}
		months.push(sails.moment().hour(1).day(1).month("January").year(sails.moment().year() + 1).format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z');
		console.log(months);

		var BookingRequest = [];
		
		for (var i = 0; i < 12; i++) {
			BookingRequest.push(Booking.find({restaurant_id: req.session.user.restaurant}).where({createdAt : {'>=': months[i], '<=': months[i + 1]}}));
		}

		var results = {
			booking : [],
		};
		
		
		Promise.all(BookingRequest)
		.then(function (reqRes) {
			for (var i = 0; i < 12; i++) {
				results.booking.push(reqRes[i].length)
			}
			
			console.log(reqRes);
			console.log(results);
			return res.view('stats/index', {stats : results});
		})
		.catch(function(err) {
			return cb(undefined, err);
		})
	}
};

