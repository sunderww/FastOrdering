/**
 * StatsController
 *
 * @description :: Server-side logic for managing Stats
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

var Promise = require('q');

module.exports = {
	
	index : function(req, res) {
		
		var getStats = function(cb) {
			var date = sails.moment(new Date(new Date().getTime() - 60 * 60 * 24 * 7 * 1000)).format('YYYY-MM-DDTHH:mm:ss.SSS');
			var results = {};
			
			return Promise.all([
				Booking.find({restaurant_id: req.session.user.restaurant}).where({'createdAt' : {'<=': date}}),
			])
			.spread(function (booking) {
				finalRes.booking = booking;
				console.log(booking);
			})
			.catch(function(err) {
				return res.serverError(err);
			})
			.done(function () {
				console.log("done");
				return cb(finalRes);
			});
		};
		
		getStats(function(results) {
			console.log(results);
			return res.json(results);
		});
	},
	
	getStats : function() {
		return Promise.all([
			Booking.find({restaurant_id: req.session.user.restaurant}).where({'createdAt' : {'<=': date}}),
		])
		.spread(function (booking) {
			results.booking = booking;
			return booking;
			console.log(booking);
		})
		.catch(function(err) {
			return res.serverError(err);
		})
		.done(function () {
			console.log("done");
			return results;
		});
	}
};

