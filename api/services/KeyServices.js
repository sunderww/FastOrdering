/**
 * KeyServices.js
 *
 * this file contains the function about key.
 */

module.exports = {

    getKeys: function(hash, cb) {
        console.log("Get Key");
        Key.find({}).exec(function (err, keys){
            if (err)
                return cb (err, null);
            if (keys)
                KeyServices.compareKeys(hash, keys, cb);
            else
                return cb (null, hash);
        });
    },
    // Functions not to use as request
    // Generate error here ??? or not ?? TODO AFTER
    generateKey: function(cb, keys){
        
        console.log("Generate Key");
        sails.bcrypt.hash(Math.random().toString(), 10, function hashGenerated(err, hash) {
            if (err) {
                console.error(err);
                return cb(err, null);
            }
            console.log("Generate Key End");
            if (keys == null)
                KeyServices.getKeys(hash, cb);
            else
                KeyServices.compareKeys(hash, keys, cb);
        });
    },
    
    compareKeys: function(hash, keys, cb) {
        
        console.log("CompareKeys");
        var key_exist = false;

        for (key in keys) {
            if (sails.bcrypt.compareSync(hash, keys[key].code)) {
                key_exist = true;
                break;
            }
        }
        if (!key_exist)
            return cb(null, hash);
        else {
            generateKey(cb, keys);
        }
    },
    
    generateCode: function(callback) {
        this.generateKey(callback);
    }
};