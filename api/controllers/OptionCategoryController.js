/**
 * OptionCategoryController
 *
 * @description :: Server-side logic for managing Optioncategories
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {

  /**
   * `OptionCategoryController.create()`
   */
  create: function (req, res) {
    if (req.method=="POST" ) {
     	OptionCategory.create({
     		name:req.param("name"),
        restaurant: req.session.user.restaurant
     	})
      .exec(function(err, optionCategory){
        if (err) {
          console.log("OptionCategory creation failed " + ret[1].ValidationError);
          req.flash('error', err.ValidationError);
        }
        else {
          console.log("OptionCategory created with success");
          req.flash('success', 'La catégorie ' + optionCategory.name + " a été créé avec succès");
        }
        OptionCategoryServices.read(req, function(ret) {return res.view({optioncategories:ret});});      
     	});
    }
    else 
      OptionCategoryServices.read(req, function(ret) {return res.view({optioncategories:ret});});      
  },

  /**
   * `OptionCategoryController.read()`
   */
  read: function (req, res) {
      OptionCategoryServices.read(req, function(ret) {
        ret.forEach(function(e){
          ret.restaurant_id = e.restaurant.id;
          delete restaurant;
        });
        return res.json({elements:ret});
      });      
  },

  read_lucas: function(req, res) {
    OptionCategoryServices.read(req, function(optionsCategory) {
      optionsCategory.forEach(function(entry){
        entry.option_ids = new Array();
        entry.option.forEach(function(e){
          entry.option_ids.push(e.id);
        });
        entry.restaurant_id = entry.restaurant.id;
        delete entry.option;
        delete entry.restaurant;        
      });
      return res.json(optionsCategory);
    });
  },


  /**
   * `OptionCategoryController.delete()`
   */
  delete: function (req, res) {
    OptionCategory
    .destroy({restaurant:req.session.user.restaurant, id:req.param("id")})
    .exec(function(err) {
      if (err) {
        console.log("Delete OptionCategory failed--> " + req.param('id'));
        req.flash('error', err.ValidationError);
      }
      else {
        console.log("Delete OptionCategory success --> " + req.param('id'));
        req.flash('success', "La catégorie a été supprimée avec succès");
      }
      res.redirect('/optioncategory/create');
    });  
  },

  /**
   * `OptionCategoryController.update()`
   */
  update: function (req, res) {
    if (req.method=="POST") {
      OptionCategory
      .update({restaurant: req.session.user.restaurant,id:req.param("id")},{name:req.param("name")})
      .exec(function(err,model) {
        if (err) {
          console.log("Failed OptionCategory update");
          req.flash('error', err.ValidationError);
        }
        else {
          console.log("OptionCategory updated with success");
          req.flash('success', "La catégorie a été mise à jour avec succès");
        }
        res.redirect('/optioncategory/create/');
      });
    }
    else
      OptionCategoryServices.read(req, function(ret) {return res.view({optioncategory:ret});});      
  }
};