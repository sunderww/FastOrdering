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
			DishServices.create(req, function(ret){
				if (!ret[0]) {
					console.log("Dish creation failed " + ret[1].ValidationError);
        			req.flash('error', ret[1].ValidationError);
				}
				else {
        			req.flash('success', "Le plat " + ret[1].name + " a été crée avec succès");
					console.log("Dish created with success");
				}
				DishServices.read(req, function(ret){return res.view(ret);});
			});
		}
		else
			DishServices.read(req, function(ret){return res.view(ret);});
   	},


    /**
     * Permet de recupérer tous les plats ou un plat spécifique si un id est présent
     *
     * @method read
     * @param {String} id id du plat(optionnel)
     * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs plats)
     */
    read: function (req, res) {
		DishServices.read(req, function(ret){
			ret.dishs.forEach(function(entry){
				entry.restaurant_id = entry.restaurant.id;
				entry.categories_id = new Array();
				entry.categories.forEach(function(e){
					entry.categories_id.push(e.id);
					delete e;
				});
				entry.options = new Array();
				entry.optioncategories.forEach(function(e){
					entry.options.push(e.id);
					delete e;
				});
				delete entry.restaurant;
				delete entry.optioncategories;
			});
			return res.json({elements: ret.dishs});
		});
    },

    delete: function (req, res) {
	    Dish.destroy({restaurant:req.session.user.restaurant, id:req.param("id")}).exec(function(err) {
	        if (err) {
		        console.log("Delete Dish failed--> " + req.param('id'));
		        req.flash('error', err.ValidationError);
	      	}
	      	else {
		        console.log("Delete Dish success --> " + req.param('id'));
		        req.flash('success', "Le plat a été supprimé avec succès");
	      	}
	      	res.redirect('/dish/create');
	    });
	},
    update: function (req, res) {
		if (req.method=="POST") {
			DishServices.update(req, function(ret){
		        if (!ret[0]) {
					console.log("Dish update failed " + ret[1].ValidationError);
          			req.flash('error', ret[1].ValidationError);
				}
				else {
          			req.flash('success', "Le plat a été mise à jour avec succès");
					console.log("Dish updated with success");
				}
				return res.redirect('/dish/create');
			});
		} else
			DishServices.read(req, function(ret){return res.view(ret);});
	}
};
