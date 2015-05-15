/**
 * Order
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */
var moment = require('moment');

module.exports = {

  attributes: {
  	
  	// restaurant_id: {
  	// 	type: 'string',
  	// 	required: true
  	// },
     date: {
                type: 'string',
          defaultsTo: function() {return moment().format("m/d/YYYY"); }
      },
      time: {
          type: 'string',
          defaultsTo: function() {return moment().format("h:mm");}
      },
  	table_id: {
  		type: 'string',
  		required: true
  	},
      diner_number :{
	  type: 'integer',
	  required: true
      },
  	waiter_id: {
  		type: 'string', // the waiter is a user object
  //		required: true
  	}, 
      comment: {type:"string"},
      orders: {
	  collection:"OrderContent",
	  via: "ordercontent_id"
      }
      
  }

};
