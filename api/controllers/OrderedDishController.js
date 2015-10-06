/**
* Controller permettant de gérer tout ce qui se rapporte au contenu de la commande (plats)
*
* @class OrderedController
* @constructor
*/

module.exports = {

  /**
  * Permet d'ajouter une commande
  *
  * @method create
  * @param {String} order_id Id de la commande
  * @param {String} dish Id du plat
  * @param {String} qty Nombre de plats
  * @param {String} comment Commentaire
  * @param {String} menuId Id du menu
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  create: function (req, res) {
	console.log(req.param("order_id"));
    OrderedDish.create({

        order_id:req.param("order_id"),
	dish_id:req.param("dish"),
	quantity:req.param("qty"),
	comment:req.param("comment"),
	menu_id:req.param("menuId")
    }).exec(function(err,model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
      else {
        return res.json({
          message: " has been created"
        });       
      }
    });
  },

  /**
  * Permet de changer le status d'une commande
  *
  * @method changeStatus
  * @param {String} order_id Id de la commande
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  changeStatus: function (req, res) {
    OrderedDish.find({id: req.param("id")}, function(err, order) {
      if (err) {
        res.send("error");
      }
      else {
        OrderedDish.update({id: req.param("id")},{status: req.param("status")}, function(error) {});
        res.send("success");
      }
    });
  },

  /**
  * Permet de recupérer tous les plats ou un plat spécifique si un id est présent
  *
  * @method read
  * @param {String} id id du plat(optionnel)
  * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs plats)
  */
  read: function (req, res) {
    if (req.param("id")) {
      OrderedDish.find({order_id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      OrderedDish.find( function(err, doc) {
        return res.send(doc);
      });
    } 
  },

  /**
  * Permet de recupérer tous les plats ou un plat spécifique si un id est présent
  *
  * @method read
  * @param {String} id id du plat(optionnel)
  * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs plats)
  */
  read_lucas: function (req, res) {
      OrderedDish.find( function(err, doc) {
        var res = array()
        doc.forEach(function(entry){

          return res.json(doc);
        });
  },



};