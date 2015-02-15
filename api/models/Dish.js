/**
 * Dish
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

  	price: {
  		type: 'float',
	    defaultsTo: 0
  	},

  	available: {
  		type: 'boolean',
  		defaultsTo: true
  	},
      dish_id: {
	  model: "Category"
      },      
  }
};
