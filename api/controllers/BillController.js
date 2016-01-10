/**
 * BillController
 *
 * @description :: Server-side logic for managing bills
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */
 var Promise = require('q');

module.exports = {
		index: function(req, res) {
			Order.find().then(function (orders){
				return res.view('bill/index', {orders : orders});
				
			}).catch(function(err) {
				res.serverError(err);
			});
		},
		
		print: function(req, res) {
			OrderedDish.find({order_id: req.param("id")}).then(function(ordered){
				var data = new Array();
				ordered.forEach(function(s){
					var dish = Dish.findOne({id:s.dish_id}).then(function(dish) {return dish})
					var menu = Menu.findOne({id:s.menu_id}).then(function(menu) {return menu});
					data.push({dish:dish, menu:menu, qty:s.qty});
				});
				return data;
			}).then(function(data){
                Restaurant.findOne({id: req.session.user.restaurant}).then(function(restaurant){
                    console.log(data);
                    return res.view('bill/print', {restaurant: restaurant, products : data});
                }).catch(function(err) {
                    return res.serverError(err);
                });
			});
		}
};

