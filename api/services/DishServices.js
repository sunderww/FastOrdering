/**
 * DishServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');

module.exports = {

	create: function(req, cb) {
		console.log("Dish creating")
		Dish.create({
			restaurant:req.session.user.restaurant,
			id: req.param("id"),
			name:req.param("name"),
			price:req.param("price")
		})
		.then(function(dish){
			var ar = new Array();
			console.log(req.param("categories_ids"));
			ar.concat(req.param("categories_ids")).forEach(function(entry){
				DishCategory.findOne({restaurant:req.session.user.restaurant,id:entry}).exec(function(err, dishCategory){
					if (dishCategory != undefined) {
						console.log(dishCategory);
						dish.categories.add(dishCategory);
						dish.save();
					}
				});
			});
			ar = new Array();
			ar.concat(req.param("optcats_ids")).forEach(function(entry){
				OptionCategory.findOne({restaurant:req.session.user.restaurant,id:entry}).exec(function(err, optionCategory){
					if (optionCategory != undefined) {
						dish.optcats.add(optionCategory);
						dish.save();
					}
				});
			});
			cb([true, dish]);
		})
		.catch(function(err) {
			cb([false, err]);
		});	
	},

	read: function(req, cb) {
		if (req.param('from'))  {
			Promise.all([
				Dish.find({restaurant:req.session.user.restaurant}).where({'createdAt' : {'>=':new Date(req.param('from'))}}).populateAll(),
				DishCategory.find({restaurant:req.session.user.restaurant}),
				OptionCategory.find({restaurant:req.session.user.restaurant})
			])
			.spread(function(dishs, categories, options){
				cb({dishs:dishs, categories: categories, optcats:options});
			});
		}		
		else if (!req.param("id")) {
			Promise.all([
				Dish.find({restaurant:req.session.user.restaurant}).populateAll(),
				DishCategory.find({restaurant:req.session.user.restaurant}),
				OptionCategory.find({restaurant:req.session.user.restaurant})
			])
			.spread(function(dishs, categories, options){
				cb({dishs:dishs, categories: categories, optcats:options});
			});
		}
		else {
			Promise.all([
				Dish.findOne({id:req.param("id"),restaurant:req.session.user.restaurant}).populateAll(),
				DishCategory.find({restaurant:req.session.user.restaurant}),
				OptionCategory.find({restaurant:req.session.user.restaurant})
			])
			.spread(function(dish, categories, options){
				cb({dish:dish, categories: categories, optcats:options});
			});
		}
	},

	update: function(req, cb) {
		if (req.param('name') != '' && req.param('price') != '') {
			Dish.destroy({id:req.param("id")})
			.then(function(dish) {
				DishServices.create(req, function(ret){
					cb(ret);
				});
			});
		}
		else {
			Dish.update({id: req.param("id")},{
				restaurant:req.session.user.restaurant,
				name:req.param("name"),
				price:req.param("price")
			}).exec(function(err, res){
				cb([false, err]);
			});
		}
	}

}