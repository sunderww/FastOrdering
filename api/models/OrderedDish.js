/**
 * OrderedDish
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
  	order_id: {
  		type: 'string',
  		required: true
  	},

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
    
    status: {
      type: 'string',
      enum: ["canceled", "ordered", "cooking", "toDeliver", "delivered"],
      defaultsTo: "ordered"
    }, 
    option_ids :{
      type:"array", 
      defaultsTo: []
      // collection: "OrderedOption",
      // via: "option"
    },
    /**
    * Id du menu
    * 
    * @property menu_id
    * @type {Integer}
    * @require true
    */
    menu_id: {type: "integer", required: true},
    
    menucomposition_id: {type: "integer", required: true},
    categorieoption_id: {type: "integer", required: true},

    /**
    * Commentaire
    * 
    * @property comment
    * @type {String}
    */ 
  	comment: 'string',
  }
};
