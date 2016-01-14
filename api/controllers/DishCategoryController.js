/**
* Controller permettant de gérer tout ce qui se rapporte au Category dans les compositions menus
* ex: MenuCompositon "plat + dessert"
* Category: {Plats [x,x,x], Dessert[y,y,y]}
*
* @class DishCategoryController
* @constructor
*/

module.exports = {

  /**
  * Permet d'ajouter une catégorie
  *
  * @method create
  * @param {String} name Nom de la catégorie
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  create: function (req, res) {
    if (req.method=="POST") {
    	DishCategory.create({
       name:req.param("name"),
       restaurant:req.session.user.restaurant,
     }).exec(function(err, dishCategory) {
      if (err) {
        console.log("DishCategory creation failed " + ret[1].ValidationError);
        req.flash('error', err.ValidationError);
      }
      else {
        console.log("DishCategory created with success");
        req.flash('success', "La catégorie d'option " + dishCategory.name + " a été créé avec succès");
      }
      DishCategoryServices.read(req, function(ret) {return res.view({categories:ret});});
    });
   }
   else
    DishCategoryServices.read(req, function(ret) {return res.view({categories:ret});});
},


  /**
  * Permet de recupérer toutes les catégories ou une spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la catégorie(optionnel)
  * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs catégorie(s))
  */
  read: function (req, res) {
    DishCategoryServices.read(req, function(ret){
      ret.forEach(function(entry){
        entry.restaurant_id = entry.restaurant.id;
        delete entry.restaurant;
      });
      return res.json({elements: ret});
    });
  },

  cats: function(req, res) {
    SessionServices.getUser(req.socket.id, function(user){
      DishCategory
      .find({restaurant:user.restaurant.id})
      .exec(function(err, dishCategories) {
        return res.json({elements:dishCategories});
      });
    });
  },

  dishcategory: function(req, res) {
    if (!req.param('restaurant'))
      return res.json('No restaurant selected');
    if (req.param('from')) {
      DishCategory
      .find({restaurant:req.param('restaurant')})
      .where({'createdAt' : {'>=':new Date(req.param('from'))}})
      .then(function(dishCategories) {
        return res.json(dishCategories);
      });
    }
    else {
      DishCategory
      .find({restaurant:req.param('restaurant')})
      .then(function(dishCategories) {
        return res.json(dishCategories);
      });
    }  
  },


  delete: function (req, res) {
    DishCategory.destroy({restaurant:req.session.user.restaurant, id:req.param("id")}).exec(function(err) {
      if (err) {
        console.log("Delete DishCategory failed--> " + req.param('id'));
        req.flash('error', err.ValidationError);
      }
      else {
        console.log("Delete DishCategory success --> " + req.param('id'));
        req.flash('success', "La catégorie d'option a été supprimée avec succès");
      }
      res.redirect('/dishcategory/create');
    });
  },

  update: function (req, res) {
    if (req.method=="POST") {
      DishCategory
      .update({restaurant:req.session.user.restaurant, id:req.param("id")}, {name:req.param("name")})
      .exec(function(err, dishCategory) {
        if (err) {
          console.log("Failed Dish update");
          req.flash('error', err.ValidationError);
        }
        else {
          console.log("Dish updated with success");
          req.flash('success', "La catégorie d'option a été mise à jour avec succès");
        }
        res.redirect('/dishcategory/create');
      });
    }
    else
      DishCategoryServices.read(req,function(ret){return res.view({category:ret});});
  }
};

