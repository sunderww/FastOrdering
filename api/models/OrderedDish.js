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
  	order: {
  		model: 'order'
  	},

    /**
    * Id du plat
    * 
    * @property dish_id
    * @type {String}
    * @require true
    */ 
  	dish: {
  		model: 'dish'
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
    options : {
      collection: "OrderedOption",
      required: false
    },
    /**
    * Id du menu
    * 
    * @property menu_id
    * @type {Integer}
    * @require true
    */
    menu: {model: "menu"},
    restaurant: {
      model: "restaurant",
      required: true
    },    
    /**
    * Commentaire
    * 
    * @property comment
    * @type {String}
    */ 
  	comment: 'string',
  },
  // enroll: function (id, cb) {

  //   OrderedOption.findOne(options.id).exec(function (err, theUser) {
  //     if (err) return cb(err);
  //     if (!theUser) return cb(new Error('User not found.'));
  //     theUser.enrolledIn.add(options.courses);
  //     theUser.save(cb);
  //   });
  // }
};
