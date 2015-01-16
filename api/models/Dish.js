/**
 * Dish
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
  	// restaurant_id: {
  	// 	type: 'string',
  	// 	required: true
  	// },

	// categories / menus in which it is contained
//  	category_id: { 
  //		type: 'string',
  //		defaultsTo: [],
//      required: true
  //	},

  	name: {
  		type: 'string',
  		required: true
  	},

  	price: {
  		type: 'float',
	    defaultsTo: 0
  	},

  	available: {
  		type: 'boolean',
  		defaultsTo: true
  	}
      
  }
};
