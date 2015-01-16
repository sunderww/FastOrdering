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
   		table_id:req.param("table"),
      waiter_id:req.param("waiter"),
      price:0
   	}).exec(function(err,model){
   		if (err) {
   			return res.json({
   				message: err.ValidationError
   			});
   		}
   		else {
   			return res.json({
   				message: req.param('name') + " has been created"
   			});  			
   		}

   	});
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


  /**
   * `OrderController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
      Order.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      Order.find( function(err, doc) {
        return res.send(doc);
      });
    } 
  }
};

