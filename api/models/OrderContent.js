/**
* OrderContent.js
*
* @description :: TODO: You might write a short summary of how this model works and what it represents here.
* @docs        :: http://sailsjs.org/#!documentation/models
*/

module.exports = {

  attributes: {
      menu_id: {
	  type:"string",
          required: true
      },
      menucomposition_id:{
          type:"string",
          required: true
      },
      dishs: {
	  model: "Dish"
      },
      quantity : {
	  type: "integer",
	  required: true
      },
      comment: {type:"string"},
      ordercontent_id: {
	  model:"Order"
      }
  }
};

