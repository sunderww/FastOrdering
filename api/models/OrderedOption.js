/**
 * OrderedOption
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
    
    /**
    * Id de la commande
    * 
    * @property order_id
    * @type {String}
    * @require true
    */   	
  	// ordered_id: {
  		// type: 'string',
      // model: "orderedDish",
  		// required: true
  	// },

    /**
    * Id du plat
    * 
    * @property dish_id
    * @type {String}
    * @require true
    */ 
  	id: {
  		type: 'string',
  		required: true,
        primaryKey: true
  	},
    
    /**
    * Nombre de plats commandés
    * 
    * @property quantity
    * @type {Integer}
    * @require true
    */
    qty: {
      type: "integer",
	    required: true
    },
    
    option : {
      type: "string",
      // model: "Option"
    },

  }
};
