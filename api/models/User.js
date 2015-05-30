/**
 * User
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

// enum UserRole { waiter, cooker, manager, admin }
var UserRole = require('../services/enums.js').UserRole;

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
        model:'key'
    },
      
    restaurant: {
        model:'restaurant'
    },
    
    isWaiter: function() {
        return ((this.role & UserRole.waiter) || (this.role & UserRole.manager));
  },

    isCook: function() {
        return ((this.role & UserRole.cook) || (this.role & UserRole.manager));
  },
      
    isManager: function() {
        return (this.role & UserRole.manager);
  },

    isAdmin: function() {
        return (this.role & UserRole.admin);
    }
  },

  /*toJSON: function() {
    var obj = this.toObject();
    delete obj.password;
    delete obj._csrf;
    return obj;
  },*/

    checkPassword: function(values) {
        var errors = [];
        
        if (!values.password || values.password != values.confirm_password)
            errors.push("Password doesn't match.");
        
        if (!values.password || values.password.length < 6)
            errors.push("Password size need to be superior to 6 characters.");
        
        return errors;
    },    
    
    beforeUpdate: function(values, cb) {
        if (values.password)
        {
            var errors = this.checkPassword(values);
        
            console.log(errors);
            if (errors.length > 0)
                return cb({err : errors});
        
            sails.bcrypt.hash(values.password, 10, function(err, hash) {
                if (err) return cb(err);
                values.password = hash;
            });
        }
        cb();
    },
    
    beforeCreate: function(values, cb) {

        var errors = this.checkPassword(values);
        
        // TODO FIX THIS
        User.find({email: values.email}).exec(function findEmail(err, found) {
            if (found.length > 0) {  
                errors.push("Email already use");
            }
            if (err)
                console.log(err);
        });
        
        console.log(errors);
        if (errors.length > 0)
            return cb({err : errors});
        
        sails.bcrypt.hash(values.password, 10, function(err, hash) {
            if (err) return cb(err);
            values.password = hash;
            cb();
        });
  }

};