/**
 * OrderedDishController
 *
 * @description :: Server-side logic for managing Ordereddishes
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `OrderedDishController.create()`
   */
  create: function (req, res) {
    OrderedDish.create({
      order_id:req.param("order"),
      dish_id:req.param("dish")
      // :0
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
   * `OrderedDishController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `OrderedDishController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },

  /**
   * `OrderedDishController.changeStatus()`
   */
  changeStatus: function (req, res) {
    console.log("gg");
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
   * `OrderedDishController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
      OrderedDish.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      OrderedDish.find( function(err, doc) {
        return res.send(doc);
      });
    } 
  }
};

