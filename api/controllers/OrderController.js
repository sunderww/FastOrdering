/**
* Controller permettant de gérer tout ce qui se rapporte au Commande
*
* @class OrderController
* @constructor
*/
var moment = require('moment');

module.exports = {


    getAll: function(req, res) {
	Order.find().exec(function(err, orders) {
	    return res.ok(orders);
	});

    },
  /**
  * Permet d'ajouter une commande
  *
  * @method create
  * @param {String} numTable Numéro de table
  * @param {String} numPA Nombre de personne
  * @param {String} globalComment Nombre de personne
  * @return {Integer} Retourne 200 si ok sinon 500 avec un message d'erreur
  */
getOneOrder: function(req, res) {
   Order.find().sort("createdAt DESC").limit(2).exec(function(err, orders){
    var result = new Array();
    orders.forEach(function(order) {
      result.push({
        "numOrder": order.id,
        "numTable": order.table_id,
        "numPA": order.dinerNumber,
        "date": order.date,
        "hour": order.time
      });
    });
    //      cb(orders);
    res.ok({orders : result});
        });
 // OrderServices.getOneOrder("55f0b125bcd880f22267d8f2", function (result) {
 //    return res.json(result);
 // });

},

    delete: function(id) {
	Order.destroy({id:id}).exec(function(err, doc) {
	    return res.ok("ok");
	});
    },

  create: function (req, res) {
	console.log("Create order");

   Order.create({
    table_id:"1",
    dinerNumber:"2",
    comments: "commande avec menu delice"
  }).exec(function(err,model){
      console.log(err);
    OrderedDish.create({
      order_id:model.id,
      dish_id:"572f78d9937726dc7ab8f8f2",
      quantity:1,
      ready:false,
      comment:"ok, chaud2",
      menu_id:"572abe8049bb4c97702057db",
    }).exec(function(err,model){
      console.log(err);
      return res.send(model);

    });
    OrderedDish.create({
      order_id:model.id,
      dish_id:"572f78d9937726dc7ab8f8f2",
      quantity:1,
      comment:"ok, chaud2",
      menu_id:"572abe8049bb4c97702057db"
    }).exec(function(err,model){
      console.log(err);
      return res.send(model);
    });
  });
},

question: function(req, res) {
  console.log("question");
//  var friendId = sails.io.sockets.clients()[0].id;

//console.log(friendId);
        var data = {date: moment().format("DD/MM/YY"),hour: moment().format("HH:mm"),msg: "J'ai une question !", numTable:"7"}
    sails.io.sockets.emit('notifications', data);
    return res.ok(data);

},

ready: function(req, res) {
  if (req.param("id")) {
    OrderedDish.findOne({id: req.param("id")}, function(err, doc) {
        var status = doc.status == "toDeliver" ? "cooking" : "toDeliver";
        OrderedDish.update({id: req.param("id")}, {status:status}, function(err, model) {
        console.log("ready");
        var friendId = sails.io.sockets.clients()[0].id;
        var data = {date: moment().format("DD/MM/YY"),hour: moment().format("HH:mm"),msg: "Le plat " + doc['name'] + "est pret!", numTable:"7"}
        sails.sockets.emit('notifications', data);
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

