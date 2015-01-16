/**
 * RoleController
 *
 * @description :: Server-side logic for managing Roles
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `RoleController.create()`
   */
  create: function (req, res) {
    Role.create({
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
   * `RoleController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `RoleController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },


  /**
   * `RoleController.read()`
   */
  read: function (req, res) {
    return res.json({
      todo: 'read() is not implemented yet!'
    });
  }
};

