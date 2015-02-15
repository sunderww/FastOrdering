/**
 * Table
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

  	waiters: {
  		type: 'array', // an array of ids
  		defaultsTo: []
  	},
      findByName: function(name) {
      }
    // ,

  	// plan_id: {
  	// 	type: 'string',
  	// 	required: true
  	// },

  	// pos: {
  	// 	// type should be pos
  	// 	// use pos.x and pos.y
  	// 	type: 'json',
  	// 	required: true
  	// }

  }

};
