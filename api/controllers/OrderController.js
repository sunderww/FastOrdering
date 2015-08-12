/**
* Controller permettant de gérer tout ce qui se rapporte au Commande
*
* @class OrderController
* @constructor
*/

module.exports = {
  /**
  * Permet d'ajouter une commande
  *
  * @method create
  * @param {String} numTable Numéro de table
  * @param {String} numPA Nombre de personne
  * @param {String} globalComment Nombre de personne
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
  create: function (req, res) {
   Order.create({
    table_id:"1",
    dinerNumber:"2",
    comments: "vite"
  }).exec(function(err,model){
    OrderedDish.create({
      order_id:model.id,
      dish_id:"5508cb2447347e023f344813",
      quantity:1,
      ready:false,
      comment:"ok, chaud2",
      menu_id:"5508c9ccfb42fcf03ea2c173"
    }).exec(function(err,model){
      console.log(err);
      return res.send(model);

    });
    OrderedDish.create({
      order_id:model.id,
      dish_id:"5508cb2447347e023f344813",
      quantity:1,
      comment:"ok, chaud2",
      menu_id:"5508c9ccfb42fcf03ea2c173"
    }).exec(function(err,model){
      console.log(err);
      return res.send(model);
    });
  });
},

question: function(req, res) {
  console.log("question");
  var friendId = sails.io.sockets.clients()[0].id;
  sails.sockets.emit('notification', {msg: "J'ai une question !", numTable:"7"});
},

ready: function(req, res) {
  if (req.param("id")) {
    OrderedDish.findOne({id: req.param("id")}, function(err, doc) {
        var status = doc.status == "toDeliver" ? "cooking" : "toDeliver";
        OrderedDish.update({id: req.param("id")}, {status:status}, function(err, model) {
        console.log("ready");
        var friendId = sails.io.sockets.clients()[0].id;
        sails.sockets.emit('notification', {msg: "Le plat " + doc['name'] + "est pret!", numTable:"7"});
        return res.send(doc);
      });
    });
  }    
},

json: function (req, res) {
  if (req.param("id")) {
    Order.find({id: req.param("id")}, function(err, doc) {
      return res.send(doc);
    });
  }
},

 /**
  * Permet de recupérer toutes les commandes ou une commande spécifique si un id est présent
  *
  * @method read
  * @param {String} id id de la commande(optionnel)
  * @return {JSON} Retourne les résultat présents en base de données (0 ou 1 ou plusieurs commandes)
  */
  // read: function (req, res) {
  //   if (req.param("id")) {
  //     Order.find({id: req.param("id")}, function(err, doc) {
  //       return res.send(doc);
  //     });
  //   } else {
  //     OrderedDish.find( function(err, doc) {
  //   	  return res.view({orders:doc});
  //     });
  //   } 
  // },
  read: function (req, res) {
    if (req.param("id")) {
      Order.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      Order.find(function(err, doc) {
        return res.view({orders:doc});
      });
    }
    
  }
};

