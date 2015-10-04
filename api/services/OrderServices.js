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
		console.log("getOneOrder");
		var ret = "Error: nothing";
		Promise.all([
			Order.findOne({id:order_id}),
			OrderedDish.find({order_id: order_id})
		])
		.spread(function(order, ordered){
			var res = new Array();
			ordered.forEach(function(entry) {
				// entry.qty = entry.quantity;
				// entry.id = entry.dish_id
				res.push({"menuId":entry.menu_id, "content": [entry]});
			});
			ret = {
				'numOrder' : order.id,
				'numTable' : order.table_id,
				'numPA': order.dinerNumber,				
				'date' : order.date,
				'hour' : order.time,
				'globalComment': order.comments,
				'order' : res
			};
		}).catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb(ret);
		});
	},
	getLastOrders: function(limit, cb) {
		console.log("getLastOrders");
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
	},

	deleteOrder: function(id, cb) {
		console.log("deleteOrder");
		Promise.all([
			Order.destroy({id:id}),
			OrderedDish.destroy({order_id:id})
		])
		.catch(function(err){
			cb(err);
		})
		.then(function(){
			return cb("ok");
		});
	},
	createOrder: function(json, cb) {
		console.log("createOrder")
		var ret;
		Promise.all([
			Order.create({
				id:id,
				table_id:json.numTable,
				dinerNumber:json.numPA,
				comments: json.globalComment
			})
			])
		.spread(function(model){
			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {
					OrderedDish.create({
						order_id:model.id,
						id:json['order'][a].content[i].id,
						qty:json['order'][a].content[i].qty,
						comment:json['order'][a].content[i].comment,
						menu_id:json['order'][a].menuId,
						options:json['order'][a].content[i].options

					}).exec(function(err,model){
						if (err)
							return cb(err);
					});
				}
			}
			ret = {numOrder: model.id, numTable: json.numTable, numPA: json.numPA, date:model.date, hour:model.time};
		}).catch(function(err){
			return cb(err);
		})
		.done(function(){
			return cb(ret);
		});
	}
}
