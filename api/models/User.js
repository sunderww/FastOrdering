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
  		unique: true
  	},

  	password: {
  		type: 'string',
  		required: true
  	},

  	username: {
  		type: 'string',
  		unique: true,
  		required: true
  	}
   //  ,

  	// subscribtionTimeLeft: {
  	// 	type: 'time',
  	// 	defaultsTo: 0
  	// }
    
  },

  isWaiter: function() {
  	return ((this.role & UserRole.waiter) || (this.role & UserRole.manager));
  },

  isManager: function() {
  	return (this.role & UserRole.manager);
  },

  isAdmin: function() {
  	return (this.role & UserRole.admin)
  }

};
