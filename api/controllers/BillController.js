/**
 * BillController
 *
 * @description :: Server-side logic for managing bills
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
		index: function(req, res) {
			OrderedDish.findOne({id: req.param("id")}).then(function(ordered){
	   			var dish = Dish.findOne({id:ordered.dish_id}).then(function(dish) {return dish.name});
	   			var socket_id = Order.findOne({id:ordered.order_id}).populate('waiter_id').then(function(user){return user.waiter_id.socket_id});
	   			var status = Order.updateStatus(ordered.id);
	   			return ["ordered", socket_id, dish, status];
   			}).spread(function(one, socket_id, dish, status){
	 			var data = {date: moment().format("DD/MM/YY"),hour: moment().format("HH:mm"),msg: "Le plat " + dish + "est pret!", numTable:"7"}
	 			sails.io.sockets.emit(socket_id, 'notifications', data);
	 			return res.json({status:status});
   			});
		}
};

