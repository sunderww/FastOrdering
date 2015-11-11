/**
 * SessionServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');

module.exports = {

 	loginFromPhone : function(json, socket_id, cb) {
		console.log("Authentication");
		var ret;
		Promise.all([
			Key.findOne({code: json.user_key})
		])
		.spread(function(key){
	        User.findOne({key:key.id}).exec(function(err, user){
		       User.update({id:user.id}, {socket_id:socket_id}).exec(function(){});
     	       sails.session.user = user;
	        })
            ret = (key == undefined || key.active == false) ? false : true;
            console.log("Access " + ((ret == false) ? "Denied" : "Granted" ));
       		if (ret == true)
	     		console.log("Socket id --> "  + socket_id);
       		ret = {"answer":ret, "restaurant_id":key.restaurant};
        })
		.catch(function(err){
			console.log(err);
		})
		.done(function(){
			return cb(ret);
		});
    },
}