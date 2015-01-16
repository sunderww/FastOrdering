/**
 * UserController
 *
 * @description :: Server-side logic for managing Users
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `UserController.create()`
   */
  create: function (req, res) {
   	User.create({
   		role:req.param("role"),
   		email:req.param("email"),
   		password:req.param("password"),
   		username:req.param("username")
   	}).exec(function(err,model){
   		if (err) {
   			return res.json({
   				message: err.ValidationError
   			});
   		}
   		else {
   			return res.json({
   				message: req.param('username') + " has been created"
   			});  			
   		}

   	});
  },


  /**
   * `UserController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `UserController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },


  /**
   * `UserController.read()`
   */
  read: function (req, res) {
   	if (req.param("id")) {
   		User.find({id: req.param("id")}, function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   		User.find( function(err, doc) {
   			return res.send(doc);
   		});
   	}
  }
};

