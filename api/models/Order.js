/**
* Model definissant l'objet Commande.
*
* @class Order
* @constructor
*/

var moment = require('moment');

module.exports = {

  attributes: {
    /**
    * Jour de la commande
    * 
    * @property date
    * @type {String}
    * @default Date.now("m/d/YYYY")
    */  	
    date: {
      type: 'string',
      defaultsTo: function() {return moment().format("DD/MM/YYYY"); }
    },

    /**
    * Heure de la commande
    * 
    * @property time
    * @type {String}
    * @default Date.now("h:mm")
    */    
      time: {
          type: 'string',
          defaultsTo: function() {return moment().format("hh:mm");}
      },
    	table_id: {
    		type: 'string',
        required: true
      },

      dinerNumber :{
        type: 'integer',
        required: true
      },
  	
      waiter_id: {
        type: 'string'//, // the waiter is a user object
  //		required: true
      },
      status: {
        type: 'string',
        enum: ["canceled", "ordered", "cooking", "toDeliver", "delivered"],
        defaultsTo: "ordered"
      },       
      comments: {type:"string"},

      // orderContent_id: {
      //   type: 'string'//,
      //   // required: true
      // }
  }
};