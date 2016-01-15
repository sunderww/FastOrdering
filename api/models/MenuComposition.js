/**
* MenuComposition.js
*
* @description :: TODO: You might write a short summary of how this model works and what it represents here.
* @docs        :: http://sailsjs.org/#!documentation/models
*/

module.exports = {
  attributes: {
      name: {type: "string",required:true },
      menu: {model:"menu", required:true},
      categories: {collection:"DishCategory", required:false},
      position: {type:'integer', required:true},
     restaurant: {
    	model: "restaurant",
    	required: true
    }  		
    }
};