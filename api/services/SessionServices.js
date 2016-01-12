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
		     sails.session.user = key.user;
  			// User.findOne({key:key.id}).populateAll().exec(function(err, user){
		       User.update({id:key.user.id}, {socket_id:socket_id}).then(function(user){ 
		       	console.log("done");
		       }).then(function(){
		       	  var ret = (key == undefined || key.active == false) ? false : true;
            console.log("Access " + ((ret == false) ? "Denied" : "Granted" ));
       		if (ret == true)
	     		console.log("Socket id --> "  + socket_id);
	     		console.log("Session --> "  + sails.session.user.email);
       		cb({"answer":ret, "restaurant_id":key.restaurant});
		       });
     	      
	        // });		
	        // return key;	
		});





		// Promise.all([
		// 	Key.findOne({code: json.user_key})
		// ])
		// .spread(function(key){
	 //        if (!key) {
	 //            console.log("Access Denied " + json.user_key);
	 //        	ret = {"answer": false};
	 //        	return;
	 //        }

	 //        User.findOne({key:key.id}).populateAll().exec(function(err, user){
		//        User.update({id:user.id}, {socket_id:socket_id}).exec(function(){});
  //    	       sails.session.user = user;
	 //        });
  //           ret = (key == undefined || key.active == false) ? false : true;
  //           console.log("Access " + ((ret == false) ? "Denied" : "Granted" ));
  //      		if (ret == true)
	 //     		console.log("Socket id --> "  + socket_id);
  //      		ret = {"answer":ret, "restaurant_id":key.restaurant};
  //       })
		// .catch(function(err){
		// 	console.log(err);
		// })
		// .done(function(){
		// 	return cb(ret);
		// });
    },
}