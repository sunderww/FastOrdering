/**
 * OrderedOption
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
   
    qty: {
      type: "integer",
      required: true
    },
    
    option : {
      model: "Option",
      required: false
    },
    ordered_dish: {
      type: "string",
      required: true
    }
  }
};
