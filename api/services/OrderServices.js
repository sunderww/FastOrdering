/**
 * OrderServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');
module.exports = {

    getOneDetail: function(ordered, cb){
	var ret = "";
	return Promise.all([
	    Dish.findOne({id:ordered.dish_id}),
	    Menu.findOne({id:ordered.menu_id})
	])
            .spread(function(dish, menu){
		console.log("GGGGGG");
                            var ready = (ordered.status == "toDeliver") ? "ready btn btn-success" : "ready btn btn-danger";
                            ret = '<div style="display:inline-block !important;width:500px;margin:0px;height:30px" class="admin-form"><h3>Menu - ' + menu.elements.name  + '</h3>        <div style="height:30px"><span class="element-zoom" style="float:left;font-size:40px;">' + ordered[e].qty + '</span><span class="element-zoom" style="width:100px; float:left !important;">' + dish[0].name + '</span><span class="element-zoom">'+ordered[e].comment+'</span></div></br><div><span id="' + ordered[e].id + '"><button  class="' + ready + ' ">Pret</button><button class="btn btn-primary question">Serveur</button></span></span></div><span></div></div>';
	    })
	    .catch(function(err){
		cb(err);
	    });
    },



    getDetails: function(req, cb){
        var ret = new Array();

	Promise.all([
	    OrderedDish.find({order_id: req.param('id_command')})
	])
	    .spread(function(ordered){
		for (var e = 0; ordered[e]; e++) {
		    ret[e] = OrderServices.getOneDetail(ordered[e]);
		};
})
	    .spread(function(){
		cb(ret);
})
.catch(function(err){
    cb(err);
})
    .done(function(){
	for(var i = 0;ret[i];i++) {
	    ret[i]
	};
	console.log("SORTIE");
	console.log(ret);
//	return cb(ret);
    });
},

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
			    'order' : order_content
			};
		}).catch(function(err){
			cb(err);
		})
		.done(function(){
		    console.log(ret.order);
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
//	    console.log(json);
		var ret;
		Promise.all([
			Order.create({
				id:id,
				table_id:json.numTable,
				dinerNumber:json.numPA,
				comments: json.globalComment
			})
			])
/*		.spread(function(){
			var orderedoption = new Array();
			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {

					if (options in json['order'][a].content[i]) {
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
		})*/
		.spread(function(model,orderedoption){
		    console.log("toto");
			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {
		//			if (typeof orderedoption[a] != 'undefined')
		//				options = orderedoption[a];
		//			else
						options = [];
						OrderedDish.create({
							orderContent_id: orderconten.id,
							order_id:model.id,
						    dish_id:json['order'][a].content[i].id,
						    qty:parseInt(json['order'][a].content[i].qty),
//							menucomposition_id:json['order'][a].content[i].menucomposition_id,
//							categorieoption_id:json['order'][a].content[i].categorieoption_id,
							comment:json['order'][a].content[i].comment,
							menu_id:json['order'][a].menuId,
							options:options
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
