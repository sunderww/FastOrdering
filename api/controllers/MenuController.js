/**
 * MenuController
 *
 * @description :: Server-side logic for managing menu
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	
  /**
   * `MenuController.create()`
   */
  create: function (req, res) {
      Menu.create({
          name:req.param("name"),
	  compo: ["54d9780d33c35f96385c8da2"],
    }).exec(function(err,model){
          if (err) {
              return res.json({
                  message: err.ValidationError
              });
          }
          else {
              return res.json({
                  message: req.param('name') + " has been created !!"
	      });
	  }

      });
  },


  /**
   * `MenuCompositionController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },


  /**
   * `MenuCompositionController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },


  /**
   * `MenuCompositionController.read()`
   */
  read: function (req, res) {
	  Menu.find( function(err, doc) {
	      return res.json({elements: doc});
	  });
  }
};

