/**
 * OrderController
 *
 * @description :: Server-side logic for managing Orders
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	

  /**
   * `OrderController.create()`
   */
  create: function (req, res) {
   	Order.create({
	    table_id:req.param("numTable"),
	    diner_number:req.param("numPA"),
	    comment: req.param("globalComment")
   	}).exec(function(err,model){
   //   		if (err) {
   // 			return res.json({
   //				message: err.ValidationError
   //			});
   //		}
   //		else {
		    console.log(model.id);
   		    return model;
   //		}
   	});
      console.log("tt");
  },


  /**
   * `OrderController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `OrderController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },

  json: function (req, res) {
    if (req.param("id")) {
      Order.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    }
    },


  /**
   * `OrderController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
      Order.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      OrderedDish.find( function(err, doc) {
	  var re;
	  for (var i = 0; doc[i];i++) {
	      Order.find({id :doc[i].order_id }, function(er, doo){
//		  doc[i].order = doo;
	      });
	  }
	  return res.json({ data: doc });
      });
    } 
  }
};

