var Promise = require('q');
module.exports = {
	
	create: function(req, cb) {
		console.log("createOption");
		var ret;
		Promise.all([
			OptionCategory.findOne({id:req.param("optioncategories_ids")}).populate("option")
		])
		.spread(function(options){
			 	console.log(0);

 			Option.create({name:req.param("name"), option: options.id}).exec(function (err, mod) {
            	console.log(1);
          
            });

            OptionCategory.update({id:req.param("optioncategories_ids")}, 
            		{option:options}).exec(function(err2, doc3){
           		console.log(2);
            });
		})
		.spread(function(){
		 
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			 Option.find( function(err, doc) {
		  	// ret = doc;
			return cb(doc);
	        });
		});
      }
}