/**
 * Category
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

//  	parent_id: {
  //		type: 'string',
  //		required: true
  //	},

  	name: {
  		type: 'string',
  		required: true
  	},
      id_cat: {
	  model: "MenuComposition"
      },
      ids : {
	   model : "Dish"
      }
  //	color: {
  //		type: 'string', // in rgb format
  		// defaultsTo: 0xffffff // default is white
  //	}
    // ,

  	// price: {
	  // 	// if the price is < 0, than this is a menu ;
	  // 	// then all items contained in this category do NOT have a price
  	// 	type: 'float',
  	// 	defaultsTo: -1
  	// }

  }

};
