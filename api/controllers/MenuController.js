/**
 * MenuController
 *
 * @description :: Server-side logic for managing menus
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	


  /**
   * `MenuController.create()`
   */
  create: function (req, res) {
      Menu.create({
          name:req.param("name"),
          collections: [4,5,6]
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
   * `MenuController.destroy()`
   */
  destroy: function (req, res) {
      Menu.destroy({id: 1}, function(e){
         // if (e) {console.log('Room.destroy error!!!!!!!!!!!!!!!!!!!!!!');}
      });
  },


  /**
   * `MenuController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },


  /**
   * `MenuController.list()`
   */
  list: function (req, res) {
      if (req.param("id")) {
          Menu.find({id: req.param("id")}, function(err, doc) {
              return res.send(doc);
	  });
      } else {
	  Menu.find( function(err, doc) {
	      return res.json({elements: doc});
	  });
      }
  }
};