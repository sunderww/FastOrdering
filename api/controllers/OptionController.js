/**
 * OptionController
 *
 * @description :: Server-side logic for managing Options
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `OptionController.create()`
   */
  create: function (req, res) {
    if (req.method=="POST" && req.param("id") == undefined) {
   	Option.create({
   		name:req.param("name"),
   		optioncategories_ids: [req.param("optioncategories_ids")]
   		}).exec(function(err, model){
   		if (err)
   			return res.json({message: err.ValidationError});
   		// else
   		// 	return res.json({message: "GG"});
   Option.find( function(err, doc) {
          return res.view({options:doc});
        });	});
    
    }
    else
      Option.find( function(err, doc) {
          return res.view({options:doc});
        });
  },


  /**
   * `OptionController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
        Option.find({id: req.param("id")} ,function(err, doc) {
          return res.send(doc);
      });
    } else {
        Option.find( function(err, doc) {
          return res.json({elements: doc});
      });
    }
  },


  /**
   * `OptionController.delete()`
   */
  delete: function (req, res) {
    Option.destroy({id:req.param("id")}).exec(function(err, doc) {
           res.json({elements: doc});
      });
      res.redirect(307, '/option/create');
  },


  /**
   * `OptionController.update()`
   */
  update: function (req, res) {
    var CATEGORIES;
    OptionCategory.find(function(err, doc) {
     CATEGORIES = doc;
    });
    if (req.method=="POST") {
     
      Option.update({id:req.param("id")},{
        name:req.param("name"),
        optioncategories_ids: [req.param("optioncategories_ids")]
          }).
      exec(function(err,model) {
            if (err) {
          return res.json({
            message: err.ValidationError
              });
            }

      });
      res.redirect(307, '/option/create/');
    }
     
    Option.findOne({id: req.param("id")} ,function(err, doc) {
          return res.view({option:doc, categories: CATEGORIES});
      }); 
  }
};

