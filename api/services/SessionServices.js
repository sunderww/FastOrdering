/**
 * SessionServices.js
 *
 * this file contains the function about key.
 */

var Promise = require('q');

module.exports = {

 	loginFromPhone : function(json, socket_id, cb) {
		console.log("Authentication ");
		var ret;
		Key
		.findOne({code: json.user_key})
		.populateAll()
		.then(function(key){
  			if (!key) {
	            console.log("Access Denied " + json.user_key);
	        	cb({"answer": false});
	        	return;
	        }
		    User.update({id:key.user.id}, {socket_id:socket_id}).then(function(user){ 
		       	console.log("done");
	     		console.log("Session --> "  + user.email);
		    }).then(function(){
		       	  var ret = (key == undefined || key.active == false) ? false : true;
            console.log("Access " + ((ret == false) ? "Denied" : "Granted" ));
       		if (ret == true)
	     		console.log("Socket id --> "  + socket_id);
       		 sails.sockets.join(socket_id, key.restaurant.id);
       		cb({"answer":ret, "restaurant_id":key.restaurant.id});
		       });
		});
    },

    getUser: function(socket_id, cb){
    	User.findOne({socket_id:socket_id}).populateAll().exec(function(err, user){
    		if (err)
    			console.log("Access denied from socket");
   			cb(user);
    	});
    },

    getUsersFromRestaurant: function(user, cb) {
    	User.find({restaurant:user.restaurant}).exec(function(){

    	});
    }
}