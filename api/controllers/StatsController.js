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
		var i;
		
		for (i = 0; i < 12; i++) {
			months.push(sails.moment().date(1).month(i).hour(1).format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z');
		}
		months.push(sails.moment().hour(1).day(1).month("January").year(sails.moment().year() + 1).format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z');
		console.log(months);

		var request = [];
		
		for (i = 0; i < 12; i++) {
			request.push(Booking.find({restaurant_id: req.session.user.restaurant}).where({createdAt : {'>=': months[i], '<=': months[i + 1]}}));
		}
		
		for (i = 0; i < 12; i++) {
			request.push(Order.find({}).where({createdAt : {'>=': months[i], '<=': months[i + 1]}, status : 'ordered'}));
		}
		
		// for (i = 0; i < 12; i++) {
		// 	request.push(OrderedDish.find({}).where({createdAt : {'>=': months[i], '<=': months[i + 1]}, status : 'delivered'}));
		// }
		
		var results = {
			booking : [],
			order : [],
			ca : [],
		};
		
		
		Promise.all(request)
		.then(function (reqRes) {
			var i;
			for (i = 0; i < 12; i++) {
				results.booking.push(reqRes[i].length);
			}
			for (i = 12; i < 24; i++) {
				results.order.push(reqRes[i].length);
			}
			// for (i = 12; i < 24; i++) {
			// 	results.ca.push(reqRes[i].price);
			// }
			
			console.log(reqRes);
			console.log(results);
			return res.view('stats/index', {stats : results});
		})
		.catch(function(err) {
			return cb(undefined, err);
		})
	}
};

