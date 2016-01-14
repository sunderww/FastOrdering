/**
 * OrderServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');
module.exports = {

	getDetails2: function(req, cb) {
		OrderedDish.find({order: req.param('id_command')}).populateAll().exec(function(err, ret){
			var res = new Object;
			var str = '<ul class="nav nav-tabs tabs-border nav-justified">';
			
				
			ret.forEach(function(entry,i){
			    if (res[entry.menu.name] == undefined) {
					if (i == 0)
						str += '<li class="active">';
					else
						str += '<li>';
						str += '<a aria-expanded="true" href="#'+entry.menu.name+'" data-toggle="tab">'+entry.menu.name+'</a>'
	            	+ '</li>';
					res[entry.menu.name] = new Array();
			    }
			    res[entry.menu.name].push(entry);
			});
			str += '</ul><div class="tab-content">';
			


			for (var i  in res) {
				
				ret = "";
				res[i].forEach(function(entry) {
		        	var s_options = "</br>";
		        	entry.options.forEach(function(option){
		        		s_options = s_options + "</br>" + option.qty + " " + option.name;
		        	});
				    var ready = (entry.status == "toDeliver") ? "btn btn-success readyy" : "btn btn-danger readyy";
		           	ret += '<div style="display:inline-block !important;width:500px;margin:0px;" class="admin-form">'
		            + '<div style="height:100px">'
		            + '<span class="element-zoom" style="float:left;font-size:40px;"> ' + entry.qty + '</span>'
		            + '<span class="element-zoom" style="padding-top:10px;width:200px;float:left !important;">' + entry.dish.name + " " + s_options 
		      		+ '</br><div style="padding-top:10px;width:200px" id="' + entry.id + '">'
		            + '<button  class="' + ready + '">Pret</button>'
		            + '<button class="btn btn-primary question">Serveur</button>'
		            + '</div>'
		            + '</span>'
		            + '<div style="padding-right:30%;padding-top:10px;" class="element-zoom">'+entry.comment+'</div>'
		            + '</div>'
		            + '</br>'
		            + '</span>'
		        	+ '</div>'
		        	;
				});

					str += '<h3 class="mt5">Description des plats</h3><hr class="short alt">';
					str += '<div id="'+res[i][0].menu.name+'" class="tab-pane active">';

				str += '<p>'
				+ ret
				+ '</p>'
                + '</div>';
                
			};
			cb(str);
      	});		
	},
    getOneDetail: function(req,cb){
		console.log("getOneDetail");
		var ret = "";
		Promise.all([
		    OrderedOption.find().where({"ordered_dish":req.param('ordered_dish')}).populate('option'),
		    Dish.findOne({id:req.param('dish_id')}),
		    Menu.findOne({id:req.param('menu_id')}),
		    Order.findOne({id:req.param('order_id')}),
		    OrderedDish.findOne({id:req.param('ordered_dish')}),
		])
        .spread(function(opt, dish, menu, order, ordered_dish){
        	var s_options = "</br>";
        	opt.forEach(function(option){
        		s_options = s_options + "</br>" + option.qty + " " + option.option.name;
        	});
		    var ready = (ordered_dish.status == "toDeliver") ? "btn btn-success readyy" : "btn btn-danger readyy";
           	ret = '<div style="display:inline-block !important;width:500px;margin:0px;" class="admin-form">'
            + '<h3>Menu - '+ menu.name + ' </h3>'
            + '<div style="height:100px">'
            + '<span class="element-zoom" style="float:left;font-size:40px;"> ' + ordered_dish.qty + '</span>'
            + '<span class="element-zoom" style="padding-top:10px;width:200px;float:left !important;">' + dish.name + " " + s_options 
      		+ '</br><div style="padding-top:10px;width:200px" id="' + ordered_dish.id + '">'
            + '<button  class="' + ready + '">Pret</button>'
            + '<button class="btn btn-primary question">Serveur</button>'
            + '</div>'
            + '</span>'
            + '<div style="padding-right:30%;padding-top:10px;" class="element-zoom">'+ordered_dish.comment+'</div>'
            + '</div>'
            + '</br>'
            + '</span>'
        	+ '</div>'
        	;
	    })
	    .catch(function(err){
			console.log(err);
	    })
	    .done(function(){
			cb(ret);
	    });
    },

	getOneOrder: function(order_id, cb){
		console.log("getOneOrder");
		var ret = "Error: nothing";
	       Promise.all([
			Order.findOne({id:order_id}),
			OrderedDish.find({order: order_id}).populate('menu'),
			OrderedOption.find()
			])
		.spread(function(order, ordered, opt){
			var res = new Object();
		    var order_content = new Array();
			ordered.forEach(function(entry) {
		 		var rr = new Array();
			 	opt.forEach(function(o) {
			 		if (o.ordered_dish == entry.id)
			 			rr.push({id:o.option, qty:o.qty});
			 	});
		 		entry.option = rr;
			    entry.qty = (entry.qty).toString();
			    entry.id = entry.dish;
			    if (entry.menu.name == "alacarte")
				    entry.menu_id = 0;
			    else
				    entry.menu_id = entry.menu.id;
			    entry.order_id = entry.order;
			    delete entry.dish;
			    delete entry.menu;
			    delete entry.order;
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
			  Order.find().sort("createdAt ASC").limit(limit)
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
		.done(function(){
			return cb("ok");
		});
	},

	createOrderedDish: function(socket_id, order, current, currentt) {
		OrderedDish.create({
					   	order:order,
					    qty:parseInt(current.qty),
						comment:current.comment,
					})
					.exec(function(err,ordered){
						 Dish.findOne({id:current.id}).then(function(dish){
						 	ordered.dish = dish;
						 	ordered.save();
						 });
						if (currentt.menuId != 0) {
							Menu.findOne({id:currentt.menuId}).then(function(menu){
								ordered.menu = menu;
							 	ordered.save();
							});
						}
						else {
							SessionServices.getUser(socket_id, function(user){
								Menu.findOne({restaurant:user.restaurant.id, name:"alacarte"}).then(function(menu){
									ordered.menu = menu;
								 	ordered.save();
								});
							});
						}
						if (typeof current.options != 'undefined') {
							for (var u = 0; u < current.options.length; u++) {
								OrderServices.createOrderOption(current.options[u], ordered);
							}
						}
					});
	},

	createOrder: function(socket_id, json, cb) {
		console.log("createOrder")
		var ret;
		
		Order.create({
			id:id,
			table_id:json.numTable,
			dinerNumber:json.numPA,
			comments: json.globalComment,
		})
		.then(function(order){
			
			SessionServices.getUser(socket_id, function(user){
				order.waiter_id = user;
				order.save();
			});

			for (var a = 0;json['order'][a]; a++) {
				for (var i = 0;json['order'][a].content[i]; i++) {
					var current = json['order'][a].content[i];
					var currentt = json['order'][a];
					OrderServices.createOrderedDish(socket_id, order, current, currentt);
				}
			}
			ret = {numOrder: order.id, numTable: json.numTable, numPA: json.numPA, date:order.date, hour:order.time};
		})
		.catch(function(err){
			console.log(err);
		})
		.done(function(){
			return cb(ret);
		});
	}
	,
	createOrderOption: function(current, ordered) {
		Promise.all([
			Option.findOne({id:current.id}),
			OrderedOption.create({qty:current.qty, ordered_dish:ordered.id}),
		])
		.spread(function(option, optionordered){
			optionordered.option = option;
			optionordered.save();
			ordered.options.add(optionordered);
			ordered.save();
		}).catch(function(err){
			console.log(err);
		})
		.done(function(){
		});		
	}
}