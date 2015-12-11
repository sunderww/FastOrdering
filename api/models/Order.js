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
          defaultsTo: function() {return moment().format("HH:mm");}
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
        model: "model",
  		  required: true
      },
      status: {
        type: 'string',
        enum: ["canceled", "ordered", "cooking", "toDeliver", "delivered"],
        defaultsTo: "ordered"
      },       
      comments: {type:"string"},
  },

  updateStatus: function(id) {
    var status = OrderedDish.findOne({id: id}).then(function(ordered) {
      var status = ordered.status == "toDeliver" ? "cooking" : "toDeliver";
      OrderedDish.update({id: id}, {status:status}, function(err) {
        if (err)
          console.log("UpdateStatus Order --> " + err);
      });
      return status;
    });
    return status;
  }
};
