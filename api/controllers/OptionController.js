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
      OptionServices.create(req, function(ret){
        if (!ret[0]) {
          console.log("Option creation failed " + ret[1].ValidationError);
          req.flash('error', ret[1].ValidationError);
        }
        else {
          console.log("Option created with success");
          req.flash('success', "L'option " + ret[1].name + " a été créé avec succès!");
        }
        OptionServices.read(req,function(ret){return res.view(ret);});
      });
    }
    else
      OptionServices.read(req,function(ret){return res.view(ret);});
  },


  /**
   * `OptionController.read()`
   */
  read: function (req, res) {
    OptionServices.read(req, function(ret){return res.json({elements: ret['options']});});
  },


  /**
   * `OptionController.delete()`
   */
  delete: function (req, res) {
    Option
    .destroy({restaurant:req.session.user.restaurant, id:req.param("id")})
    .exec(function(err, doc) {
      if (err) {
        console.log("Delete Option failed--> " + req.param('id'));
        req.flash('error', err.ValidationError);
      }
      else {
        console.log("Delete Option success --> " + req.param('id'));
        req.flash('success', "L'option a été supprimée avec succès");
      }
      res.redirect('/option/create');
    });
  },

  /**
   * `OptionController.update()`
   */
   update: function (req, res) {
    if (req.method=="POST") {
      OptionServices.update(req, function(ret){
        if (!ret[0]) {
          console.log("Option update failed");
          req.flash('error', ret[1].ValidationError);
        }
        else {
          console.log("Option updated with success");
          req.flash('success', "La catégorie a été mise à jour avec succès");
        }
          return res.redirect('/option/create');
      });
    } else
      OptionServices.read(req, function(ret){return res.view(ret);});
  }
};