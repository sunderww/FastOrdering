/**
* Model definissant l'objet Plat.
*
* @class Dish
* @constructor
*/


module.exports = {

  attributes: {
    restaurant: {
      model: 'restaurant',
      required: true
    },  
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
    categories: {
      collection: "dishCategory",
      required: false
    },
    optcats: {
      collection: "optionCategory",
      required: false
    }    
  }
};
