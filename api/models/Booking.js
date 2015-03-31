/**
 * Booking
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
  	date: { 
  		type: 'datetime',
  		required: true
  	},

    name: {
      type: 'string',
      required: true
    },
      
    nb_persons: {
        type: 'integer',
        required: true
    },

  	restaurant_id: {
  		type: 'string',
  		required: true
  	}
    
  }

};
