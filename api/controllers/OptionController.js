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
    if (req.method=="POST") {
     OptionServices.create(req, function(){
        if (false) {
          console.log("Option creation failed");
          return res.json({message: err.ValidationError});
        }
        else
          console.log("Option created with success");
        Option.find( function(err, options) {
          return res.view({options:options});
        });
     });
    }
    else {
      Option.find( function(err, options) {
          return res.view({options:options});
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
      console.log("Delete Option --> " + req.param('id'));
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

