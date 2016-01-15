module.exports = {
	create: function(req, cb) {

		MenuComposition.create({
  			name:req.param("name"),
      		position: req.param('position'),
      		restaurant:req.session.user.restaurant
		})
		.exec(function(err, menuComposition){
			if (err)
				return cb([false, err]);
			else {
				Menu.findOne({id:req.param("menu_id")}).exec(function(err, menu){
					if (!err) {
						menuComposition.menu = menu;
						menuComposition.save();
					}
				});
				
				var ar = new Array();
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
			}
			cb([true, menuComposition]);
		});
		// .catch(function(err) {
		// 	console.log(err);
		// 	cb([false, err]);
		// })
		// .done(function() {
		// });
	}
}