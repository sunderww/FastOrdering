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
      menu_id:req.param("menu_id"),
      categories_ids: req.param("categories_ids")
    }).exec(function(err, model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
      else {
        return res.json({
          message: model.id
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

