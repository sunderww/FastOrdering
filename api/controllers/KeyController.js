/**
 * KeyController
 *
 * @description :: Server-side logic for managing keys
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    
    
    // ADD ERROR FLASH AND OK
    create: function(req, res) {
        if (req.session.user) {
            KeyServices.generateCode(function codeGenerated(err, code){
                console.log("??" + err + code);
                if (err)
                    return res.serverError(err);
                if (code) {
                    Key.create({code: code, restaurant: req.session.user.restaurant}).exec(function keyCreated(err, key) {
                        if (err)
                            return res.serverError(err)
                        res.redirect('/user');
                    });
                }
            });
        }
    },
    /*create: function(req, res) {
        
        if (req.session.user) {
            var code = 0;
            var ok = true;
            var generateAgain = false;
            
            KeyController.generateKey(function (err, newcode){
                if (err)
                    return res.serverError(err);
                code = newcode;
            });

            console.log(code);

            Key.find({}).exec(function (err, keys) {
                while (ok) {
                    for (key in keys) {
                        if (KeyController.compareKey(code, key, function (err, result){
                            if (err)
                                return res.serverError(err);
                            return result;
                        }))
                            generateAgain = true;
                        }
                    if (generateAgain)
                        KeyController.generateKey(function (err, newcode){
                        if (err)
                            return res.serverError(err);
                        code = newcode;
                        });
                    else
                        ok = false;
                }
                console.log(code);
                Key.create({code: code, restaurant: req.session.user.restaurant}).exec(function keyCreated(err, key) {
                    if (err) {
                        console.error(err);
                        return res.serverError(err); // TODO FLASH ERROR
                    }
                    res.redirect('/user');
                });
            });
        }
    },*/
    
    destroy: function (req, res) {
      Key.findOne({id: req.param('id')}).exec(function(err, user) {
          if (err)
              return res.serverError(err);
          if (!key)
              return res.notFound();
      });
      
      Key.destroy(req.param('id')).exec(function Destroyed(err) {
        if (err)
            return res.serverError(err);
      });
      
      return res.redirect('/user');
    },
        
};

