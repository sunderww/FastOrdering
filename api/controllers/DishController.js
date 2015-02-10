/**
 * DishController
 *
 * @description :: Server-side logic for managing Dishes
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `DishController.create()`
   */
  create: function (req, res) {
   	Dish.create({
   		name:req.param("name"),
   		price:req.param("price"),
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
   * `DishController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `DishController.update()`
   */
  update: function (req, res) {
      return res.send("HELLLLLO");
  },


  /**
   * `Dish.read()`
   */
   read: function (req, res) {
   	if (req.param("id")) {
   	    Dish.find({id: req.param("id")} ,function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   	    Dish.find( function(err, doc) {		    
   		    return res.json({elements: doc});
   		});
   	}
   }  
};

