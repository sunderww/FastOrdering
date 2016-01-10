var Promise = require('q');
module.exports = {
	
	update: function(id,req,  cb) {
		var data;
		var ret = false;
		Promise.all([
			MenuComposition.find({menu:id,restaurant:req.session.user.restaurant}).populateAll(),
			MenuComposition.find({restaurant:req.session.user.restaurant}).populateAll(),
			Menu.findOne({id:id}).populateAll(),
			DishCategory.find({restaurant:req.session.user.restaurant})
		])
		.spread(function(menuComposition, allComposition, menu, dishCategory){
			if (req.param('menu_name') != undefined) {
				Menu.update({id:req.param('id')}, {name:req.param('menu_name'), price:req.param('price')}, function(){});
				ret = true;				
			}
			data = {"dish_category":dishCategory, "compositions":allComposition, "selected_compositions":menuComposition, "menu": menu};
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			cb([data, ret]);
		});
      }
}