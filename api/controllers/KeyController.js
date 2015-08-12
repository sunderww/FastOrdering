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

