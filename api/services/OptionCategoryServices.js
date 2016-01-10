module.exports = {
	read: function(req, cb ) {
    	if (req.param('from')) {
	    	OptionCategory
	    	.find({restaurant:req.session.user.restaurant})
	    	.where({'createdAt' : {'>=':new Date(req.param('from'))}})
	    	.populateAll()
	    	.exec(function(err, optionCategories) {
	    		cb(optionCategories);
	    	});
    	}
    	else if (!req.param('id')) {
	    	OptionCategory
	    	.find({restaurant:req.session.user.restaurant})
	    	.populateAll()
	    	.exec(function(err, optionCategories) {
	    		cb(optionCategories);
	    	});
    	}
    	else {
	    	OptionCategory
	    	.findOne({id:req.param('id'), restaurant:req.session.user.restaurant})
	    	.populateAll()
	    	.exec(function(err, optionCategory) {
	    		cb(optionCategory);
	    	});
    	}
	}
}