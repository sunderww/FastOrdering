/**
 * RestaurantController
 *
 * @description :: Server-side logic for managing Restaurants
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

 module.exports = {



  /**
   * `RestaurantController.create()`
   */
   create: function (req, res) {
   	Restaurant.create({
   		name:req.param("name")
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
   * `RestaurantController.destroy()`
   */
   destroy: function (req, res) {
   	return res.json({
   		todo: 'destroy() is not implemented yet!'
   	});
   },


  /**
   * `RestaurantController.update()`
   */
   update: function (req, res) {
   	return res.json({
   		todo: 'update() is not implemented yet!'
   	});
   },


  /**
   * `RestaurantController.read()`
   */
   read: function (req, res) {
   	if (req.param("id")) {
   		Restaurant.find({id: req.param("id")}, function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   		Restaurant.find( function(err, doc) {
   			return res.send(doc);
   		});
   	}
   }
};

