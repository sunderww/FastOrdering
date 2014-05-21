/**
 * Dish
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

	// categories / menus in which it is contained
  	categories: { 
  		type: 'array',
  		defaultsTo: []
  	},

  	name: {
  		type: 'string',
  		required: true
  	},

  	price: {
  		type: 'float',
  		defaultsTo: -1
  	},

  	available: {
  		type: 'boolean',
  		defaultsTo: true
  	}
    
  }

};
