/**
 * UserController
 *
 * @description :: Server-side logic for managing Users
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */


module.exports = {
	

  'register': function (req, res) {
    var errflash = _.clone(req.session.flash);
    res.view('user/register', {flash : errflash});
    req.session.flash = {};
  },

  /**
   * `UserController.create()`
   */
  create: function (req, res) {

      User.create(req.params.all()).exec(function userCreated(err, user) {
      if (err) {
        console.log(err);
        req.session.flash = {
          err: err
        }
        
        return res.redirect('/register');
      }

      res.json(user);
      req.session.flash = {};
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
      User.update(req.param('id'), req.params.all()).exec(function Updated(err, updated){
          if (err)
              res.redirect('/user/edit/' + req.param('id')); // TODO ERROR GESTION (flash err)
          
          res.redirect('/user');
      });
  },

  edit: function(req, res) {
     User.findOne({id: req.param('id')}).exec(function(err, user) {
        if (err)
            return res.serveError(err);
        if (!user)
            return res.notFound();
         
        res.view('user/edit', {user: user});
    });
  },
    
  show: function(req, res) {
    User.findOne({id: req.param('id')}).exec(function(err, user) {
        if (err)
            return res.serveError(err);
        if (!user)
            return res.notFound();
        
        res.view('user/show', {user: user});
    });
  },
    
  index: function(req, res) {
    User.find({}).exec(function foundUsers(err, users) {
        if (err)
            return res.serveError(err);
        
        res.view('user/index', {users: users});
    });
  }
};

