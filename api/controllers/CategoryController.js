/**
 * CategoryController
 *
 * @description :: Server-side logic for managing Categories
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {

  /**
   * `CategoryController.create()`
   */
  create: function (req, res) {
   	Category.create({
   		name:req.param("name"),
	    collections : [6]
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
   * `CategoryController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `CategoryController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },


  /**
   * `CategoryController.read()`
   */
  list: function (req, res) {
   	if (req.param("id")) {
   		Category.find({id: req.param("id")}, function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   		Category.find( function(err, doc) {
   		    return res.json({elements: doc});
   		});
   	}
  }
};

