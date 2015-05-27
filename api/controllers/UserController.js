/**
 * UserController
 *
 * @description :: Server-side logic for managing Users
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
	

  'register': function (req, res) {
    res.locals.flash = _.clone(req.session.flash);
    res.view();
    req.session.flash = {};
  },

  /**
   * `UserController.create()`
   */
  create: function (req, res) {

      User.create(req.params.all(), function userCreated(err, user) {
      if (err) {
        console.log(err);
        req.session.flash = {
          err: err
        }

        return res.redirect('/user/register');
      }

      res.json(user);
      req.session.flash = {};
   	});
  },

  login: function (req, res) {
    res.view();
  },


  /**
   * `UserController.destroy()`
   */
  /*destroy: function (req, res) {
    return res.json({
      todo: 'destroy() is not implemented yet!'
    });
  },*/


  /**
   * `UserController.update()`
   */
  /*update: function (req, res) {
    return res.json({
      
    });
  },*/


  /**
   * `UserController.read()`
   */
  /*read: function (req, res) {
   	if (req.param("id")) {
   		User.find({id: req.param("id")}, function(err, doc) {
   			return res.send(doc);
   		});
   	} else {
   		User.find( function(err, doc) {
   			return res.send(doc);
   		});
   	}
  }*/
};

