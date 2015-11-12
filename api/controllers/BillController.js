/**
 * BillController
 *
 * @description :: Server-side logic for managing bills
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */
 var Promise = require('q');

module.exports = {
		index: function(req, res) {
			// Add status
			// OrderedDish.findAll().then(function(order) {
			// 	var dish = Dish.findOne({id:ordered.dish_id}).then(function(dish) {return dish.name});
			// };
			Order.find().then(function (orders){
				return res.view('bill/index', {orders : orders});
				
			}).catch(function(err) {
				res.serverError(err);
			});
			// }).then(function(orders) {
			// 	for (var i = 0; i < orders.length; i++) {
			// 		OrderedDish.find({order_id: orders[i].id}).then(function(orderedDish) {
			// 			orders[i].ordered_dish = orderedDish;
			// 	  	});
			// 	}
			// 	return orders;
			// }).then(function(orders) {
			// 	Promise.all([
			// 		OrderedOption.find().where({ordered_dish: req.param('ordered_dish')}).populate('option'),
			// 		Dish.findOne({id:req.param('dish_id')}),
			// 		Menu.findOne({id:req.param('menu_id')}),
			// 		Order.findOne({id:req.param('order_id')}),
			// 		OrderedDish.findOne({id:req.param('ordered_dish')}),
			// 	])
			// 	.spread(function(opt, dish, menu, order, ordered_dish){
			// 		var s_options = "</br>";
			// 		opt.forEach(function(option){
			// 			s_options = s_options + "</br>" + option.qty + " " + option.option.name;
			// 		});
			// })
			// .catch(function(err) {
			// 	res.serverError(err);
			// });
		},
		
		print: function(req, res) {
			OrderedDish.find({order_id: req.param("id")}).then(function(ordered){
				var data = new Array();
				ordered.forEach(function(s){
					var dish = Dish.findOne({id:s.dish_id}).then(function(dish) {return dish.name})
					var menu = Menu.findOne({id:s.menu_id}).then(function(menu) {return menu.name});
					data.push({dish:dish, menu:menu, qty:s.qty});
				});
				return [data];
			}).then(function(data){
				console.log(data);
			});
		}
};

