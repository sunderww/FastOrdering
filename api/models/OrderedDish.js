/**
 * OrderedDish
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

// enum DishStatus { canceled, ordered, cooking, toDeliver, delivered }
var DishStatus = require('../services/enums.js').DishStatus;

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

  	comment: 'string',
  	status: {
  		type: 'int',
    	defaultsTo: DishStatus.ordered
    }
  }

};
