/**
 * Restaurant
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {

  	name: { 
  		type: 'string',
  		required: true
  	},
      
    users: {
        collection: 'user',
        via: 'restaurant'
    },
      
    keys: {
        collection: 'key',
        via: 'restaurant'
    },
    
    adress: {
        type: 'string',
        required: true
    },
    
    postalcode: {
        type: 'string',
        required: true
    },
    
    phone: {
        type: 'string',
        required: true
    },
    
    siret: {
        type: 'string',
        required: true
    },
    
    rcs: {
        type: 'string',
        required: true
    },

  	// plan_id: 'string',

  	// user_id: {
  	// 	type: 'string',
  	// 	required: true
  	// }

  },

  // beforeCreate: function(values, next) {
  // 	Plan.create({}).done(function(err, plan) {
  // 		values.plan_id = plan.id;
  // 	});
  // }

};
