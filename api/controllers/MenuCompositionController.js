/**
 * MenuCompositionController
 *
 * @description :: Server-side logic for managing menucompositions
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	
  /**
   * `MenuCompositionController.create()`
   */
  create: function (req, res) {
      MenuComposition.create({
          name:req.param("name"),
	  price:req.param("price"),
	  cat: ["54d977db78928280386cfcf7"]
    }).exec(function(err, model){
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
	  MenuComposition.find( function(err, doc) {
	      return res.json(doc);
	  });
  }
};

