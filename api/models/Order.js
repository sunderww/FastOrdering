/**
 * Order
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
  	restaurant_id: {
  		type: 'string',
  		required: true
  	},

  	table_id: {
  		type: 'string',
  		required: true
  	},

  	waiter_id: {
  		type: 'string', // the waiter is a user object
  		required: true
  	}, 

  	price: {
  		type: 'float',
  		required: true
  	}
    
  }

};
