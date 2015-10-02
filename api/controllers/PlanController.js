/**
 * PlanController
 *
 * @module      :: Controller
 * @description	:: A set of functions called `actions`.
 *
 *                 Actions contain code telling Sails how to respond to a certain type of request.
 *                 (i.e. do stuff, then send some JSON, show an HTML page, or redirect to another URL)
 *
 *                 You can configure the blueprint URLs which trigger these actions (`config/controllers.js`)
 *                 and/or override them with custom routes (`config/routes.js`)
 *
 *                 NOTE: The code you write here supports both HTTP and Socket.io automatically.
 *
 * @docs        :: http://sailsjs.org/#!documentation/controllers
 */

module.exports = {

	state: [],

    create: function (req, res) {
    Plan.create({
      name:req.param("title"),
      dimX:req.param("dimX"),
      dimY:req.param("dimY"),
      numShapes:req.param("numShapes"),
      position:"not yet"
    }).exec(function(err,model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
      else {
      	console.log(req.param("title") + "a été sauvegardé");
        return res.redirect('/plan');       
      }

    });
  },

  index: function (req, res) {
  		return res.view('plan/plan');
  },

  /**
   * `PlanController.destroy()`
   */
  destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },

  /**
   * `PlanController.update()`
   */
  update: function (req, res) {
    return res.json({
      todo: 'update() is not implemented yet!'
    });
  },

  /**
   * `PlanController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
      Table.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      Table.find( function(err, doc) {
        return res.send(doc);
      });
    }
  },

  findOne: function(req, res) {
  	if (req,param("title")) {
  		Plan.find({name: req.param("title")}, function(err, doc) {
  			return res.send(doc);
  		});
  	} else {
  		Table.find( function(err, doc) {
  			return res.send(doc);
  		});
  	}
  }

  /**
   * Overrides for the settings in `config/controllers.js`
   * (specific to PlanController)
   */
 // _config: {}
  
};
