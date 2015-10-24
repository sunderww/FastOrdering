/**
 * OrderServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');
module.exports = {


	getOneOrder: function(order_id, cb){
		console.log("getOneOrder");
		var ret = "Error: nothing";
		Promise.all([
			Order.findOne({id:order_id}),
			OrderedDish.find({order_id: order_id})
			])
		.spread(function(order, ordered){
			var res = new Object();
			var order_content = new Array();
			ordered.forEach(function(entry) {
				entry.qty = (entry.qty).toString();
				entry.id = entry.dish_id;
				delete entry.dish_id;
				if (res[(entry.menu_id).toString()] == undefined)
					res[(entry.menu_id).toString()] = new Array();
				res[(entry.menu_id).toString()].push(entry);
			});

			for (var i in res){
				order_content.push({"menuId":i, "content": res[i]});
			}
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
		.spread(function(){
			var orderedoption = new Array();
			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {
					if (typeof json['order'][a].content[i].options != 'undefined') {
						OrderedOption.create({
							qty:json['order'][a].content[i].options['qty'],
							option:json['order'][a].content[i].options['id']
						}).exec(function(err, mod){
							if (err)
								return cb(err);
							orderedoption[a] = mod;
						});
					}
				}
			}
		})
		.spread(function(model,orderedoption){
			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {
					if (typeof orderedoption[a] != 'undefined')
						options = orderedoption[a];
					else
						options = [];
						
						OrderContent.create({
							menuComposition_id: json['order'][a].content[i].menucomposition_id,
							order_id: model.id
						}).exec(function(err, orderconten){
						OrderedDish.create({
							orderContent_id: orderconten.id,
							order_id:model.id,
							dish_id:json['order'][a].content[i].id,
							qty:json['order'][a].content[i].qty,
							menucomposition_id:json['order'][a].content[i].menucomposition_id,
							categorieoption_id:json['order'][a].content[i].categorieoption_id,
							comment:json['order'][a].content[i].comment,
							menu_id:json['order'][a].menuId,
							options:options
						}).exec(function(err,model){
							if (err)
								return cb(err);
						});
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
