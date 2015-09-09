/**
 * OptionController
 *
 * @description :: Server-side logic for managing Options
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */
	var utf8 = require('utf8');
	var encoding = require('encoding');
module.exports = {
  /**
   * `OptionController.create()`
   */
   create: function (req, res) {
    if (req.method=="POST" && req.param("id") == undefined) {
      OptionCategory.findOne({id:req.param("optioncategories_ids")}).populate("option").exec(function(err, doc){
        if (!err) {
          var OPTIONCATEGORY = doc.option;
          Option.create({name:req.param("name"), option: doc.id}).exec(function (err3, doc2, doc) {
            OPTIONCATEGORY.push(doc2.id);
              OptionCategory.update({id:req.param("optioncategories_ids")}, {option:OPTIONCATEGORY}).exec(function(err2, doc3){
                Option.find( function(err, doc) {
                  return res.view({options:OPTIONCATEGORY});
                }); 
            });
          });

        }
      });
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
        optioncategories_ids: req.param("optioncategories_ids")
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

