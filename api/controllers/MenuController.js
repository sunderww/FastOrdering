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
      }).exec(function(err,model){
        if (err) {
          return res.json({
            message: err.ValidationError
          });
        }
        else {
             Menu.find( function(err, doc) {
          return res.view({menus:doc});
        });
        }
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
          if (data[1] == true)
           return res.redirect('/menu/create');
          else {
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
        if (req.param("id")) {
      Menu.findOne({id:req.param("id")}, function(err, doc) {
        return res.json({elements: doc});
      });  
    }
	  Menu.find({name: { '!' : ["alacarte"]}}, function(err, doc) {
	      return res.json({elements: doc});
	  });
  },

  getMenu : function(res, req) {
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

