/**
 * Controller permettant de gérer tout ce qui se rapporte au Plat
 *
 * @class DishController
 * @constructor
 */
module.exports = {

    /**
     * Permet d'ajouter un plat
     *
     * @method create
     * @param {String} name Nom du plat
     * @param {String} price Prix du plat
     * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
     */
    create: function (req, res) {
		if (req.method=="POST") {
			Dish.create({
				id: req.param("id"),
				name:req.param("name"),
				price:req.param("price"),
				categories_ids: req.param("categories_ids"),
				optioncategories_ids: req.param("optioncategories_ids")
			    }).exec(function(err,model) {
				    if (err) {
				    	console.log(err.ValidationError);
					return res.json({
						message: err.ValidationError
					    });
				    }
			else {
				Dish.find( function(err, doc) {
		return res.view({dishs:doc});
   	});
			}
		});
	}else {

	Dish.find( function(err, doc) {
		return res.view({dishs:doc});
   	});
	}
    },



//     getSocketID: function(req, res) {
// 	console.log("ttt");
// 	return res.ok('My socket ID is: ');
// },


    /**
     * Permet de recupérer tous les plats ou un plat spécifique si un id est présent
     *
     * @method read
     * @param {String} id id du plat(optionnel)
     * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs plats)
     */
    read: function (req, res) {
	if (req.param("id")) {
		    Dish.find({id: req.param("id")} ,function(err, doc) {
			    return res.send(doc);
			});
		} else {
		    Dish.find( function(err, doc) {
			doc.forEach(function(entry) {
				entry.createdAt = new Date("1995-12-17T03:24:00");
				entry.updatedAt = new Date("1995-12-17T03:24:00");
			    entry.options = entry.optioncategories_ids;
			    delete entry.optioncategories_ids;
			});
		    return res.json({elements: doc});
			});
		}
    },
    delete: function (req, res) {
	  	console.log(req.param('id'));
	    Dish.destroy({id:req.param("id")}).exec(function(err, doc) {
			res.redirect('/dish/create');
		});
	},
    update: function (req, res) {
		var CATEGORIES;
		var OPTIONCATEGORIES;
		DishCategory.find(function(err, doc) {
			CATEGORIES = doc;
		});
		OptionCategory.find(function(err, doc) {
			OPTIONCATEGORIES = doc;
		});
		if (req.method=="POST") {
			Dish.update({id:req.param("id")},{
				name:req.param("name"),
				price:req.param("price"),
				categories_ids: req.param("categories_ids"),
				optioncategories_ids: req.param("optioncategories_ids")
			    }).exec(function(err,model) {
				    if (err) {
					return res.json({
						message: err.ValidationError
					    });
				    }
			});
			res.redirect('/dish/create');
		} else {

		    Dish.findOne({id: req.param("id")} ,function(err, doc) {
			    return res.view({dish:doc, categories: CATEGORIES, optioncategories:OPTIONCATEGORIES});
			});		   
		}
	}
};
