/**
 * Plan
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
  	size: {
  		// type should be 'size' but this does not exist
  		// use type.width and type.height
  		// width and height are in percentage to render
  		type: 'json',
  		defaultsTo: { width: 100, height: 100 }
  	},

    tables: function(cb) {
      Table.find({plan_id: this.id}).done(function (err, tables) {
        cb(err, tables);
      })
    }

    
  }

};
