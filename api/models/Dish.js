/**
* Model definissant l'objet Plat.
*
* @class Dish
* @constructor
*/


module.exports = {

  attributes: {
   
    /**
    * Nom du plat
    * 
    * @property name
    * @type {String}
    */
  	name: {
  		type: 'string',
  		required: true
  	},

    /**
    * Prix du plat
    * 
    * @property price
    * @type {Float}
    * @default 0
    */
    price: {
  		type: 'float',
	    defaultsTo: 0
  	},

    /**
    * Disponibilité du plat
    * 
    * @property available
    * @type {Boolean}
    * @default true
    */
  	available: {
  		type: 'boolean',
  		defaultsTo: true
  	},

    // dish_id: {
	   //   model: "Category"
    // },      
    categories_ids: {
      type: "array",
      defaultsTo: []
    }
  }
};
