/**
 * UserController
 *
 * @description :: Server-side logic for managing Users
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

var UserRole = require('../services/enums.js').UserRole;

module.exports = {

  'register': function (req, res) {
    var errflash = _.clone(req.session.flash);
    res.view('user/register', {flash : errflash});
    req.session.flash = {};
  },

  /**
   * `UserController.createRestaurant()`
   */
  createRestaurant: function (req, res) {

      var values = req.params.all();
      
      values.role = UserRole.manager; // Create an Manager
      
      User.create(req.params.all()).exec(function userCreated(err, user) {
        if (err) {
            console.log(err);
            req.session.flash = {
                err: err
            }
            return res.redirect('/register');
        }

        // Should I do that in afterCreate ??           // Not sure if user should be add DOC UNCLEAR
        Restaurant.create({name: req.param('restaurant'), users: user.id}).exec(function restaurantCreated(err, restaurant){
            if (err) {
                console.log(err);
                return res.serverError(err);
            }
    
            User.update({id: user.id}, {restaurant: restaurant.id}).exec(function userUpdated(err, user) {
                if (err) {
                    console.log(err);
                    return res.serverError(err);
                }
            });
            
        });
        // Redirect and Login
        res.json(user); 
        req.session.flash = {};
   	});
  },

  /**
   * `UserController.createWaiter()`
   */
  createWaiter: function(req, res) {
      
      var values = req.params.all();
      
      values.role = UserRole.waiter; // Create an Manager
    
      User.create(req.params.all()).exec(function userCreated(err, user) {
        if (err) {
            console.log(err);
            req.session.flash = {
                err: err
            }
            return res.redirect('/register');
        }
        res.json(user);
        res.session.flash = {};  
      });
  },

  /**
   * `UserController.destroy()`
   */
  destroy: function (req, res) {
      User.findOne({id: req.param('id')}).exec(function(err, user) {
          if (err)
              return res.serverError(err);
          if (!user)
              return res.notFound();
      });
      
      User.destroy(req.param('id')).exec(function Destroyed(err) {
        if (err)
            return res.serverError(err);
      });
      
      return res.redirect('/user');
  },


  /**
   * `UserController.update()`
   */
  update: function (req, res) {
      User.update(req.param('id'), req.params.all()).exec(function Updated(err, updated){
          if (err)
              res.redirect('/user/edit/' + req.param('id')); // TODO ERROR GESTION (flash err)
          
          res.redirect('/user');
      });
  },

  /**
   * `UserController.edit()`
   */
  edit: function(req, res) {
     User.findOne({id: req.param('id')}).exec(function(err, user) {
        if (err)
            return res.serverError(err);
        if (!user)
            return res.notFound();
         
        res.view('user/edit', {user: user});
    });
  },

  /**
   * `UserController.show()`
   */
  show: function(req, res) {
    User.findOne({id: req.param('id')}).exec(function(err, user) {
        if (err)
            return res.serverError(err);
        if (!user)
            return res.notFound();
        
        res.view('user/show', {user: user});
    });
  },
 
  /**
   * `UserController.index()`
   */
  index: function(req, res) {
      
    if (req.session.user)
    {
        
        if (req.session.user.role == UserRole.admin) {
            User.find({}).exec(function foundUsers(err, users) {
                if (err)
                    return res.serverError(err);
        
                res.view('user/index', {users: users});
            });
        }
        if (req.session.user.role == UserRole.manager) {
            User.find({restaurant: req.session.user.restaurant}).exec(function foundUsers(err, users) {
                if (err) {
                    console.error(err);
                    return res.serverError(err);
                }
                
                // Key availables
                Key.find({restaurant: req.session.user.restaurant, active: false}).exec(function foundKeys (err, keys) {
                    if (err) {
                        console.error(err);
                        return res.serverError(err);
                    }
                    
                    res.view('user/index', {users: users, keys:keys});
                });
            });
        }
    }
    else
        res.redirect('/login');
  }
};

