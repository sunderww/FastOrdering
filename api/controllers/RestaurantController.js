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
   * `UserController.show()`
   */
   edit: function(req, res) {
       Restaurant.findOne({id: req.session.user.restaurant}).exec(function(err, restaurant) {
           if (err)
               return res.serverError(err);
           if (!restaurant)
               return res.notFound();
           
           res.view('restaurant/edit', {restaurant: restaurant});
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
       Restaurant.update(req.session.user.restaurant, req.params.all()).exec(function Updated(err, updated){
           if (err) {
               return res.redirect('/restaurant/edit/'); // TODO ERROR GESTION (flash err)
           }
           
           return res.redirect('/restaurant/edit/');
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

