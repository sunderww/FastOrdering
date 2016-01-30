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
          console.log("OptionCategory creation failed " + err.ValidationError);
          req.flash('error', err.ValidationError);
        }
        else {
          console.log("OptionCategory created with success " + optionCategory.id);
          req.flash('success', 'La catégorie ' + optionCategory.name + " a été créé avec succès");
        }
        OptionCategoryServices.read(req, function(ret) {return res.view({optcats:ret});});      
     	});
    }
    else 
      OptionCategoryServices.read(req, function(ret) {return res.view({optcats:ret});});      
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

  options: function(req, res) {
      if (req.param('from')) {
        SessionServices.getUser(req.socket.id, function(user){
          OptionCategory
          .find({restaurant:user.restaurant.id})
          .where({'createdAt' : {'>=':new Date(req.param('from'))}})
          .exec(function(err, optionCategories) {
            return res.json({elements:optcats});
          });
        });
      }
      else if (!req.param('id')) {
        SessionServices.getUser(req.socket.id, function(user){
          OptionCategory
          .find({restaurant:user.restaurant.id})
          .populate('option')
          .exec(function(err, optionCategories) {
            return res.json({elements:optionCategories});
          });
        });
      }
  },

  optioncategory: function(req, res) {
    if (!req.param('restaurant'))
      return res.json('No restaurant selected');
    if (req.param('from')) {
      OptionCategory
      .find({restaurant:req.param('restaurant')})
      .populate('option')
      .where({'createdAt' : {'>=':new Date(req.param('from'))}})
      .then(function(optioncategory) {
        optioncategory.forEach(function(entry){
          entry.option_ids = new Array();
          entry.option.forEach(function(e){
            entry.option_ids.push(e.id);
          });
        });
        return res.json(optioncategory);
      });
    }
    else {
      OptionCategory
      .find({restaurant:req.param('restaurant')})
      .populate('option')
      .then(function(optioncategory) {
        optioncategory.forEach(function(entry){
          entry.option_ids = new Array();
          entry.option.forEach(function(e){
            entry.option_ids.push(e.id);
          });
        });
        return res.json(optioncategory);
      });
    }    
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
          console.log("OptionCategory updated with success "+ req.param('id'));
          req.flash('success', "La catégorie a été mise à jour avec succès");
        }
        res.redirect('/optioncategory/create/');
      });
    }
    else
      OptionCategoryServices.read(req, function(ret) {return res.view({optioncategory:ret});});      
  }
};