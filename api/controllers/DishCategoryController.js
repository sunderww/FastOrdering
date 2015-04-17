/**
* Controller permettant de gérer tout ce qui se rapporte au Category dans les compositions menus
* ex: MenuCompositon "plat + dessert"
* Category: {Plats [x,x,x], Dessert[y,y,y]}
*
* @class CategoryController
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
     	}).exec(function(err,model){
     		if (err) {
     			return res.json({
     				message: err.ValidationError
     			});
     		}
     		else {
     			// res.json({
     			// 	message: req.param('name') + " has been created"
     			// });  			
     		}

     	});
    }
    // else
    // {
    DishCategory.find( function(err, doc) {
            return res.view({categories:doc});
          });
    // }
  },


  /**
  * Permet de recupérer toutes les catégories ou une spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la catégorie(optionnel)
  * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs catégorie(s))
  */
  read: function (req, res) {
   	if (req.param("id")) {
   		DishCategory.find({id: req.param("id")}, function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   		DishCategory.find( function(err, doc) {
   		    return res.json({elements: doc});
   		});
   	}
  },
    delete: function (req, res) {
        DishCategory.destroy({id:req.param("id")}).exec(function(err, doc) {
           res.json({elements: doc});
      });
        res.redirect(307, '/dishcategory/create');
    },
    update: function (req, res) {
    if (req.method=="POST") {
     console.log(req.param("id"));
      DishCategory.update({id:req.param("id")},{
        name:req.param("name")
          }).exec(function(err,model) {
            if (err) {
          return res.json({
            message: err.ValidationError
              });
            }
      });
      res.redirect(307, '/dishcategory/create');
    }
        DishCategory.findOne({id: req.param("id")} ,function(err, doc) {
          return res.view({category:doc});
      });      
  }
};

