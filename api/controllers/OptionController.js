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
     OptionServices.create(req, function(){
        Option.find( function(err, doc) {
          return res.view({options:doc});
        });
     });
    }
    else {
      Option.find( function(err, doc) {
          return res.view({options:doc});
        });
    }
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
      return res.redirect('/option/create');
    });
  },


  /**
   * `OptionController.update()`
   */
  update: function (req, res) {
      if (req.method=="POST") {
          OptionServices.post_update(req, function(){
            return res.redirect('/option/create');
          });
      }
      else {
        OptionServices.pre_update(req, function(options){
          return res.view({option:options['option'], categories: options['options']});
        });
      }
  }
};

