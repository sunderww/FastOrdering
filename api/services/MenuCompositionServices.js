module.exports = {
	create: function(req, cb) {
		var ret;

		MenuComposition.create({
  			name:req.param("name"),
      		position: req.param('position'),
      		restaurant:req.session.user.restaurant
		})
		.then(function(menuComposition){
			ret = menuComposition;
			Menu.findOne({id:req.param("menu_id")}).exec(function(err, menu){
				if (!err) {
					menuComposition.menu = menu;
					menuComposition.save();
				}
			});
			
			var ar = new Array();
			console.log(req.param("categories_ids"));
			ar.concat(req.param("categories_ids")).forEach(function(entry){
				DishCategory.findOne({id:entry}).exec(function(err, category){
					console.log(category);
					if (!err) {
						console.log(entry);
						menuComposition.categories.add(category);
						menuComposition.save();
					}
				});
			});
		})
		.catch(function(err) {
			console.log(err);
			cb([false, err]);
		})
		.done(function() {
			cb([true, ret]);
		});
	}
}