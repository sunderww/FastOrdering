/**
 * User
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

// enum UserRole { waiter, cooker, manager, admin }
var UserRole = require('../services/enums.js').UserRole;
var bcrypt = require('bcrypt');

module.exports = {

  attributes: {
  	
  	role: {
  		type: 'integer',
  		defaultsTo: 0
  	},

  	email: {
  		type: 'string',
  		email: true,
  		unique: true,
      required: true
  	},

  	password: {
  		type: 'string',
  		required: true
  	},

    key: {
      type: 'string',
      required: true
    }
   //  ,

  	// subscribtionTimeLeft: {
  	// 	type: 'time',
  	// 	defaultsTo: 0
  	// }
    
  },

    beforeCreate: function(values, cb) {

        var errors = [];
        
        /*if (!values.password || values.password != values.confirm_password)
            return cb({err: ["Password doesn't match."]});

        if (!values.password || values.password.length < 6)
            return cb({err: ["Password size need to be superior to 6 characters."]});
        
        User.findOne({email:values.email}).exec(function findEmail(err, found) {
            if (found)
                return cb({err: ["Email already use"]});
        }) */
        
        if (!values.password || values.password != values.confirm_password)
            errors.push("Password doesn't match.");

        if (!values.password || values.password.length < 6)
            errors.push("Password size need to be superior to 6 characters.");
        
        User.find({email:values.email}).exec(function findEmail(err, found) {
            if (found.length > 0) {  
                console.log("WTF ???");
                errors.push("Email already use");
            }
            if (err)
                console.log(err);
        })
        
        console.log(errors);
        if (errors)
            return cb({err : errors})
        
        bcrypt.hash(values.password, 10, function(err, hash) {
            if (err) return cb(err);
            values.password = hash;
            cb();
        });
  },
    
  isWaiter: function() {
  	return ((this.role & UserRole.waiter) || (this.role & UserRole.manager));
  },

  isManager: function() {
  	return (this.role & UserRole.manager);
  },

  isAdmin: function() {
  	return (this.role & UserRole.admin)
  },

  toJSON: function() {
    var obj = this.toObject();
    delete obj.password;
    delete obj._csrf;
    return obj;
  }

};
