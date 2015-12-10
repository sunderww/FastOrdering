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
    MenuComposition.create({
      name:req.param("name"),
      menu_id:req.param("menu_id"),
      categories_ids: req.param("categories_ids"),
      position: req.param('position')
    }).exec(function(err, model){
      if (err) {
        console.log("MenuComposition creation failed");
        return res.json({message: err.ValidationError});
      }
      console.log("DishCategory created with success");
      return res.json({message: model.id});
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
      MenuComposition.find({menu_id:req.param("id")}, function(err, doc) {
        return res.json({elements: doc});
      });  
    }
    else {
      MenuComposition.find( function(err, doc) {
        return res.json({elements: doc});
      });      
    }
  },

  /**
  * Permet de recupérer une composition spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la composition
  * @return {JSON} Retourne le résultat présent en base de données 
  */
  delete: function (req, res) {
    if (req.param("id")) {
      MenuComposition.destroy({id:req.param("id")}, function(err, doc) {
        console.log("Delete MenuComposition --> " + req.param('id'));
        return res.json({elements: doc});
      });  
    }
  }  
};

