/**
 * BillController
 *
 * @description :: Server-side logic for managing bills
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */
 var Promise = require('q');

module.exports = {
		index: function(req, res) {
			Order.find({restaurant: req.session.user.restaurant}).sort('createdAt DESC').then(function (orders){
                return orders;
            }).then(function (orders) {
                OrderedDish.find({restaurant: req.session.user.restaurant}).populateAll().then(function (data) {
                    return res.view('bill/index', {orders : orders, products: data});
                });
            }).catch(function(err) {
				res.serverError(err);
			});
		},
		
		print: function(req, res) {
            
            OrderedDish.find({order: req.param("id")}).populateAll().then(function(doc){
              return doc;
            }).then(function(data) {
                Restaurant.findOne({id: req.session.user.restaurant}).then(function(restaurant) {
                    return res.view('bill/print', {restaurant: restaurant, products : data});
                });
            }).catch(function(err) {
                return res.serverError(err);
            });
		}
};

