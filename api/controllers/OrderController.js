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
    	Order.destroy({}).exec(function(res, doc) {
    	    return res.ok("ok");
    	});

    },

question: function(req, res) {
  console.log("question");
  if (req.param("id")) {
    OrderedDish.findOne({id: req.param("id")}).then(function(ordered){
        var user = Order.findOne({id:ordered.order})
      .then(function(order){return order.waiter_id;})
      .then(function(order){return User.findOne({id:order}).then(function(user) {return user.socket_id});});
        var numTable = Order.findOne({id:ordered.order}).then(function(order){return order.table_id;});
        return [user, numTable];
    }).spread(function(socket_id, numTable){
      var data = {date: moment().format("DD/MM/YY"),hour: moment().format("HH:mm"),msg: "J'ai une question !", numTable:numTable}
      sails.sockets.emit(socket_id, 'notifications', data);
      return res.json({status:"ok"});
    });
  }
  else
    return null;

},

ready: function(req, res) {
    console.log("Order ready");
  if (req.param("id")) {
    OrderedDish.findOne({id: req.param("id")})
    .populate('order')
    .populate('dish')
    .then(function(ordered){
     //    var dish = Dish.findOne({id:ordered.dish})
	    // .then(function(dish) {return dish.name});
     //    var order = Order.findOne({id:ordered.order})
	    // .then(function(order){return order;});

        // var user = Order.findOne({id:ordered.order})
	    // .then(function(order){return order.waiter_id;})
	    // .then(function(waiter_id){return 
        var user = User.findOne({id:ordered.order.waiter_id}).then(function(user) {return user.socket_id});

        // var numTable = Order.findOne({id:ordered.order}).then(function(order){return order.table_id;});
        var status = Order.updateStatus(ordered);
        return ["ordered", user, ordered.dish.name, status, ordered.order.table_id, ordered.status];
    }).spread(function(one, socket_id, dish, new_status, numTable, current_status){
        var data = {date: moment().format("DD/MM/YY"),hour: moment().format("HH:mm"),msg: "Le plat " + dish + " est pret!", numTable:numTable}
        if (current_status == "cooking")
          sails.sockets.emit(socket_id, 'notifications', data);
        else
          new_status = current_status;
        return res.json({status:new_status});
    });
  }
  else
    return null; 
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
  read: function (req, res) {
    if (req.param("id")) {
      Order.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      Order.find(function(err, orders) {
        // orders.forEach(function(entry){
        //   entry.
        // });

        return res.view({orders:orders});
      });
    }
    
  },

  getToday: function(req, res) {
    Order.find()
//.where({"date": moment().format("DD/MM/YYYY")})
.sort("createdAt DESC").exec(function(err, doc) {
      return res.view({orders:doc});
     });
  },

  gettodayy: function(req, res) {
    Order.find()
//.where({"date": moment().format("DD/MM/YYYY")})
.sort("createdAt DESC").exec(function(err, doc) {
      return res.ok(doc);
    });
  },

  read_xavier: function(req, res) {
	 OrderServices.getLastOrders(5, function(result){
	    return res.ok(result);
	});
    },

    getDetails: function(req, res){
      OrderedDish.find({order: req.param('id_command')}, function(err, ret){
        return res.json(ret);
      });
    },
    getDetailsOrder: function(req, res) {
      OrderServices.getOneDetail(req,function(result){
    	    return res.json(result);
    	});
    },

};


