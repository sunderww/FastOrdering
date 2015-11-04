var Promise = require('q');
module.exports = {
	

	update: function(id,req,  cb) {
		console.log("Update Menu");
		var data;
		var ret = false;
		Promise.all([
			MenuComposition.find({menu_id:id}),
			MenuComposition.find(),
			Menu.findOne({id:id})
		])
		.spread(function(menuComposition, allComposition, menu){
			if (req.param('menu_name') != undefined) {
				Menu.update({id:req.param('id')}, {name:req.param('menu_name'), price:req.param('price')}, function(){});
				ret = true;				
			}
			data = {"compositions":allComposition, "selected_compositions":menuComposition, "menu": menu};
		})
		.catch(function(err){
			cb(err);
		})
		.done(function(){
			cb([data, ret]);
		});
      }
}