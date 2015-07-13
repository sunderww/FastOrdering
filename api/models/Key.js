/**
* Key.js
*
* @description :: TODO: You might write a short summary of how this model works and what it represents here.
* @docs        :: http://sailsjs.org/#!documentation/models
*/

module.exports = {

  attributes: {
      code: { 
        type: 'string',
        unique: true,
  		required: true
  	},      
      
    restaurant: {
        model: 'restaurant',
    },
      
    user: {
        model: 'user'
    },
      
    active: {
        type: 'boolean',
        defaultsTo: false
    }
  },
    
    // Generate error here ??? or not ?? TODO AFTER
    generateKey: function(cb){
        var number = Math.random().toString();
        
        sails.bcrypt.hash(number, 10, function(err, hash) {
            if (err) {
                console.error(err);
                return cb(err, null);
            }
        number = hash;
        cb(number, null);
        });
    },
    
    compareKey: function(new_key, key, cb) {
        sails.bcrypt.compare(new_key, key.code, function (err, valid){
            if (err) {
                console.error(err);
                return cb(err, null);
            }
            if (valid)
                return cb(null, true);
            return cb(null, false);
        });
    },
};

