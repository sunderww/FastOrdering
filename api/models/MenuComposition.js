/**
* MenuComposition.js
*
* @description :: TODO: You might write a short summary of how this model works and what it represents here.
* @docs        :: http://sailsjs.org/#!documentation/models
*/

module.exports = {
  attributes: {
      name: "string",
      menu: {model:"menu", required:false},
      categories: {collection:"DishCategory", required:false},
      position: 'integer',
     restaurant: {
    	model: "restaurant",
    	required: true
    }  		
    }
};