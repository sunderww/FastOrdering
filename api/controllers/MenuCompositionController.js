/**
* Controller permettant de gérer tout ce qui se rapporte à la composition des menus
* ex: "Plat + dessert" ou "dessert"
*
* @class MenuCompositionController
* @constructor
*/

module.exports = {
	
  /**
  * Permet d'ajouter une composition menu
  *
  * @method create
  * @param {String} name Nom de la composition (ex:"plat + dessert")
  * @param {String} price Prix de la composition
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  create: function (req, res) {
    MenuCompositionServices.create(req, function(ret){
      if (!ret[0]) {
        console.log("MenuComposition creation failed");
        return res.json({message: ret[1].ValidationError});
      }
      else {
        console.log("MenuComposition created with success");
        return res.json({message: ret[1].id});
      }
    });
  },

  /**
  * Permet de recupérer une composition spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la composition
  * @return {JSON} Retourne le résultat présent en base de données 
  */
  read: function (req, res) {
    if (req.param("id")) {
      MenuComposition.find({menu_id:req.param("id")}).populateAll().exec(function(err, doc) {
        return res.json({elements: doc});
      });  
    }
    else {
      MenuComposition.find({restaurant:req.session.user.restaurant}).populateAll().exec(function(err, doc) {
        doc.forEach(function(e){
          e.menu_id = e.menu.id;
          e.restaurant_id = e.restaurant.id;
          e.categories_ids = new Array();
          e.categories.forEach(function(en){
            e.categories_ids.push(en.id);
            delete en;
          });
          delete e.categories;
        });
        return res.json({elements: doc});
      });      
    }
  },

  compos: function(req, res) {
    SessionServices.getUser(req.socket.id, function(user){
      MenuComposition.find({restaurant:user.restaurant.id}).populate('categories').exec(function(err, ret){
        ret.forEach(function(e){
          e.menu_id = e.menu;
          e.categories_ids = new Array();
          e.categories.forEach(function(en){
            e.categories_ids.push(en.id);
            delete en;
          });
          delete e.categories;
          delete e.menu;
        });
        return res.json({elements: ret});        
      })
    });
  },

  /**
  * Permet de recupérer une composition spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la composition
  * @return {JSON} Retourne le résultat présent en base de données 
  */
  delete: function (req, res) {
    // if (req.param("id")) {
      MenuComposition.destroy({}, function(err, doc) {
        console.log("Delete MenuComposition --> " + req.param('id'));
        return res.json({elements: doc});
      });  
    // }
  }  
};

