/**
 * OrderedDish
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

// enum DishStatus { canceled, ordered, cooking, toDeliver, delivered }

module.exports = {

  attributes: {
  	
  	order_id: {
  		type: 'string',
  		required: true
  	},

  	dish_id: {
  		type: 'string',
  		required: true
  	},
      quantity: {
	  type: "integer",
	  required: true
      },
      menu_id: {type: "integer", required: true},

  	comment: 'string',
  }

};
