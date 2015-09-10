/**
 * OrderServices.js
 *
 * this file contains the function about key.
 */
var Promise = require('q');
module.exports = {
// {
//     "numOrder": "1",
//     "numTable": "7",
//     "numPA": "2",
//     "date": "01/01/2001",
//     "hour": "12h12",
//     "globalComment": "blablabla"
//     "order": [
//         {
//             "menuId": "2",
//             "content": [
//                 {
//                     "id": "id_dish",
//                     "qty": "2",
//                     "comment": "blabla",
//                     "status": "0",
// 		    "options": [
//                         {
//                             "id": "idsaignant",
//                             "qty": "2"
//                         },
//                         {
//                             "id": "id33cl",
//                             "qty": "2"
//                         }
//                     ]
// 		}
//             ],
//         }
//     ]
// }
	getOneOrder: function(order_id, cb){
		var ret = "Error: nothing";
		Promise.all([
			Order.findOne({id:order_id}),
			OrderedDish.find({order_id: order_id})
		])
		.spread(function(order, ordered){
			ordered.forEach(function(entry) {
				entry.qty = entry.quantity;
				entry.id = entry.dish_id
			});
				ret = {
				'numOrder' : order.id,
				'numTable' : order.table_id,
				'numPA': order.dinerNumber,				
				'date' : order.date,
				'hour' : order.time,
				'globalComment': order.comments,
				'order' : [{
				"menuId" : ordered[0].menu_id,
				"content": ordered
				}]
			};
		}).catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb(ret);
		});
	},
	getLastOrders: function(limit, cb) {
		var ret = new Array();

		Promise.all([
			  Order.find().sort("createdAt DESC").limit(limit)
		])
		.spread(function(orders){
			orders.forEach(function(order) {
   				ret.push({
   					"numOrder": order.id,
            		"numTable": order.table_id,
            		"numPA": order.dinerNumber,
            		"date": order.date,
            		"hour": order.time
            	});
			});
		}).catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb(ret);
		});
	}
}
