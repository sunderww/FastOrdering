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
        // compo: req.param("compositions_ids"),
      }).exec(function(err,model){
        if (err) {
          return res.json({
            message: err.ValidationError
          });
        }
        else {
      //     MenuComposition.create({
      //       name:req.param("name"),
      //       price:req.param("price"),
      //       menu_id:model['id'],
      //       categories_ids: req.param("categories_ids")
      //     }).exec(function(err, model){
      //     if (err) {
      //         return res.json({
      //             message: err.ValidationError
      //         });
      //     }
      //     else {
      //         return res.json({
      //             message: req.param('name') + " has been created"
      //   });
      //  }
      // });
        }
      }); 
      }
    //  if (req.param("id")) {
    //   Menu.find({id:req.param("id")}, function(err, doc) {
    //     return res.view({menu: doc});
    //   });  
    // }
        Menu.find( function(err, doc) {
          return res.view({menus:doc});
        });
  },
    
 /**
  * Permet de recupérer un menu spécifique si un id est présent
  *
  * @method read
  * @param {String} id id du menu
  * @return {JSON} Retourne le résultat présent en base de données
  */
  update: function (req, res) {
    var MENU;
    var COMPOSITONS;
    // if (req.method=="POST") {
    //   for (var i = 0; i < req.param("compositions_ids").length; i++) {
    //   };
    // }
    MenuComposition.find(function(err, doc) {
      COMPOSITONS = doc;
    });
    if (req.param("id")) {
      Menu.findOne({id:req.param("id")}, function(err, doc) {
        MENU = doc;
        MenuComposition.find({menu_id:req.param("id")}, function(err, doc) {
          return res.view({menu:MENU, selected_compositions:doc, compositions:COMPOSITONS});
        });
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
  read: function(req, res) {
        if (req.param("id")) {
      Menu.findOne({id:req.param("id")}, function(err, doc) {
        return res.json({elements: doc});
      });  
    }
	  Menu.find({name: { '!' : ["alacarte"]}}, function(err, doc) {
	      return res.json({elements: doc});
	  });
  },
    delete: function(req, res) {
        Menu.destroy({id:req.param("id")}).exec(function(err, doc) {
           res.json({elements: doc});
      });
      res.redirect(307, '/menu/create');
    },
    

 /**
  * Permet de recupérer le menu alacarte
  *
  * @method read
  * @return {JSON} Retourne le menu a la carte 
  */
  alacarte: function(req, res) {
    Menu.findOne({name:"alacarte"}).exec(function(err, doc){
  	  MenuComposition.find({menu_id:doc.id}).exec(function(err, result){
        id = 0;
        compos = new Array();
        if (result.length > 0) {
          for (var i = result.length - 1; i >= 0; i--) {
            for (var a =  result[i].categories_ids.length - 1; a >= 0; a--) {
              compos.push( result[i].categories_ids[a]);
            };
          };
          id = doc.id;
        }
        res.json({elements: {id:id, name:"alacarte", compo:compos}});
      });
    });
  }
};

