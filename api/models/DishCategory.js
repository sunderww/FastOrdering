/**
 * DishCategory
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	restaurant: {
  		model: 'restaurant',
  		required: true
  	},
    name: {
      type: 'string',
      required: true
    },
    real_name: {
      type: 'string',
      required: true
    }
  }
};
