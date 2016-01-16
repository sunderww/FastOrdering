module.exports = {

	create: function(req, cb) {
		console.log("Option created")
		Option.create({
			id: req.param("id"),
			name:req.param("name"),
			restaurant: req.session.user.restaurant
		})
		.then(function(option){
			var ar = new Array();
			ar.concat(req.param("optioncategories_ids")).forEach(function(entry){
				OptionCategory
				.findOne({restaurant: req.session.user.restaurant, id:entry})
				.exec(function(err, optionCategory){
					if (!err && optionCategory != undefined) {
						optionCategory.option.add(option);
						optionCategory.save();
					}
				});
			});
			return cb([true, option]);
		})
		.catch(function(err) {
			return cb([false, err]);
		});
	},

	update: function(req, cb) {
		if (req.param('name') != '') {
			Option
			.destroy({restaurant:req.session.user.restaurant,id:req.param("id")})
			.then(function(dish) {
				OptionServices.create(req, function(ret){
					cb(ret);
				});
			});
		}
		else {
			Option.update({id: req.param("id")},{
				name:req.param("name"),
				restaurant: req.session.user.restaurant
			}).exec(function(err, res){
				cb([false,err]);
			});
		}
	},

	read: function(req, cb) {
		if (req.param('from')) {
			Option
			.find({restaurant:req.session.user.restaurant})
	    	.where({'createdAt' : {'>=':new Date(req.param('from'))}})
			.populateAll()
			.then(function(options) {
	        	OptionCategory
	        	.find({restaurant:req.session.user.restaurant})
	        	.populateAll()
	        	.then(function(optioncategories){
	        		cb({options:options, optioncategories:optioncategories});
	        	});
	      	});
		}
		else if (!req.param('id')) {
			Option
			.find({restaurant:req.session.user.restaurant})
			.populateAll()
			.then(function(options) {
	        	OptionCategory
	        	.find({restaurant:req.session.user.restaurant})
	        	.populateAll()
	        	.then(function(optioncategories){
	        		cb({options:options, optioncategories:optioncategories});
	        	});
	      	});
		}
		else {
			Option
			.findOne({id:req.param('id'),restaurant:req.session.user.restaurant})
			.populateAll()
			.then(function(option) {
	        	OptionCategory
	        	.find({restaurant:req.session.user.restaurant})
	        	.populateAll()
	        	.then(function(optioncategories){
	        		cb({option:option, optioncategories:optioncategories});
	        	});
	      	});
		}
	},
}