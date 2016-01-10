module.exports = {
	read: function(req, cb ) {
    	if (req.param('from')) {
	    	DishCategory
	    	.find({restaurant:req.session.user.restaurant})
	    	.where({'createdAt' : {'>=':new Date(req.param('from'))}})
	    	.populateAll()
	    	.exec(function(err, dishCategories) {
	    		cb(dishCategories);
	    	});
    	}
    	else if (!req.param('id')) {
	    	DishCategory
	    	.find({restaurant:req.session.user.restaurant})
	    	.populateAll()
	    	.exec(function(err, dishCategories) {
	    		cb(dishCategories);
	    	});
    	}
    	else {
	    	DishCategory
	    	.findOne({id:req.param('id'), restaurant:req.session.user.restaurant})
	    	.populateAll()
	    	.exec(function(err, dishCategory) {
	    		cb(dishCategory);
	    	});
    	}
	}
}