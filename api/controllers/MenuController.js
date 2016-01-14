/**
 * MenuController
 *
 * @description :: Server-side logic for managing menu
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	
  /**
  * Permet d'ajouter un menu
  *
  * @method create
  * @param {String} name Nom du menu
  * @param {Array[String]} compo Id des différentes composition
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  create: function (req, res) {
    if (req.method=="POST") {
      Menu.create({
        name:req.param("menu_name"),
        price: req.param('price'),
        restaurant:req.session.user.restaurant
      }).exec(function(err,model){
        if (err) {
          console.log("Menu creation failed");
          req.flash('error', ret[1].ValidationError);
        }
        else {
          console.log("Menu created with success");
          req.flash('success', "Le menu " + ret[1].name + " a été crée avec succès");
        }
        Menu.find(function(err, doc) {return res.view({menus:doc});});
      }); 
      }
      else {
           Menu.find( function(err, doc) {
          return res.view({menus:doc});
        });
      }
     
  },
    
 /**
  * Permet de recupérer un menu spécifique si un id est présent
  *
  * @method read
  * @param {String} id id du menu
  * @return {JSON} Retourne le résultat présent en base de données
  */
  update: function (req, res) {
   MenuServices.update(req.param('id'), req, function(data){
          if (data[1] == true) {
            console.log("Menu updated with success");
            return res.redirect('/menu/create');
          }
          else {
            console.log("Failed Menu updated");
            return res.view(data[0]);
          }
   });
  },
 /**
  * Permet de recupérer un menu spécifique si un id est présent
  *
  * @method read
  * @param {String} id id du menu
  * @return {JSON} Retourne le résultat présent en base de données
  */
  read: function(req, res) {
    if (req.param('from')) {
      Menu.find({name: { '!' : ["alacarte"]}, restaurant:req.session.user.restaurant}).where({'createdAt' : {'>=':new Date(req.param('from'))}}).exec(function(err, doc){
        doc.forEach(function(e){
          e.menu_id = e.id;
          e.restaurant_id = e.restaurant;
          delete e.restaurant;
        });
        return res.json({elements: doc});
      });
    }
    else {
      Menu.find({name: { '!' : ["alacarte"]}, restaurant:req.session.user.restaurant}).exec(function(err, doc){
        doc.forEach(function(e){
          e.restaurant_id = e.restaurant;
          delete e.restaurant;
        });
        return res.json({elements: doc});
      });
    }
  },

  menus: function(req, res) {
    if (req.param('from')) {
      SessionServices.getUser(req.socket.id, function(user){
        Menu.find({name: { '!' : ["alacarte"]}, restaurant:user.restaurant.id}).where({'createdAt' : {'>=':new Date(req.param('from'))}}).sort("position DESC").exec(function(err, doc){
          doc.forEach(function(e){
            e.menu_id = e.id;
            e.restaurant_id = e.restaurant;
            delete e.restaurant;
          });
          return res.json({elements: doc});
        });
      });
    }
    else {
    SessionServices.getUser(req.socket.id, function(user){
      Menu.find({name: { '!' : ["alacarte"]}, restaurant:user.restaurant.id}).sort("position DESC").exec(function(err, doc){
        return res.json({elements: doc});
      });
    });
    }
  },

  menu: function(req, res) {
      if (!req.param('restaurant'))
        return res.json('No restaurant selected');
      if (req.param('from')) {
        Menu
        .find({restaurant:req.param('restaurant')})
        .where({'createdAt' : {'>=':new Date(req.param('from'))}})
        .then(function(menu) {
          return res.json(menu);
        });
      }
      else {
        Menu
        .find({restaurant:req.param('restaurant')})
        .then(function(menu) {
          return res.json(menu);
        });
    }
  },

    delete: function(req, res) {
        Menu.destroy({id:req.param("id")}).exec(function(err, doc) {
          console.log("Delete Menu --> " + req.param('id'));
        return res.redirect('/menu/create');
      });
    },
    

 /**
  * Permet de recupérer le menu alacarte
  *
  * @method read
  * @return {JSON} Retourne le menu a la carte 
  */
  alacarte: function(req, res) {
    SessionServices.getUser(req.socket.id, function(user){
      Menu.findOne({restaurant:user.restaurant.id, name:"alacarte"}).exec(function(err, doc){
        MenuComposition.find({menu:doc.id}).populate('categories').exec(function(err, result){
          var compos = new Array();
          result.forEach(function(e){
            e.categories.forEach(function(en){
              compos.push(en.id);
            });
          });
          return res.json({elements: {id:0, name:"alacarte", compo:compos}});
        });
      });
    });
  }
};

