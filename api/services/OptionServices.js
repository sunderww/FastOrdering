var Promise = require('q');
module.exports = {
	
	create: function(req, cb) {
		Promise.all([
			OptionCategory.findOne({id:req.param("optioncategories_ids")}),
			Option.create({name:req.param("name")})
		])
		.spread(function(options, option){
          	if (!option)

          	options.option.add(option);
            options.save();
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb();
      });
	},

	pre_update: function(req, cb){
		var ret = new Array();
		Promise.all([
			OptionCategory.find().populate("option"),
			Option.findOne({id: req.param("id")}),
		])
		.spread(function(options, option){
    		ret = {'option': option, 'options': options};
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb(ret);
      	});
	},

	post_update: function(req, cb) {
		Promise.all([
			OptionCategory.findOne({id:req.param("optioncategories_ids")}),
    		Option.destroy({id:req.param("id")})
		])
		.spread(function(options){
			Option.create({id:req.param('id'), name:req.param('name')}).exec(function(err, option){
				options.option.add(option);
    			options.save();
			});
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			return cb(null);
      	});		
	}
}