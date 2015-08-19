/**
 * SessionController
 *
 * @description :: Server-side logic for managing sessions
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
    
    'login': function(req, res) {
        var errflash = _.clone(req.session.flash);
        res.view('session/login', {flash : errflash});
        req.session.flash = {};
    },
    
    'trylogin': function(req, res) {
        
        // If there is no email OR no password
        if (!req.param('email') || !req.param('password')) {
            
            var emailPasswordRequired = [{name: 'emailPasswordRequired', message: 'You must enter an email and password.'}];

            req.session.flash = {err: emailPasswordRequired};
            return res.redirect('/login');
        }
        
        // Try to find the email link to the user
        User.findOne({email: req.param('email')}).exec(function findOneEmail(err, user) {
            if (err) {
                console.error(err);
                return res.serveError(err); // TODO : I don't know if it is the best way ??
            }
           
            // If there is NO user
            if (!user) {
                var invalidPasswordEmail = [{name: 'invalidPasswordEmail', message: 'Invalid combination of Email / Password'}];
                
                req.session.flash = {err: invalidPasswordEmail};
                return res.redirect('/login');
            }
            
            // Try to match password
            sails.bcrypt.compare(req.param('password'), user.password, function (err, valid){
                if (err) {
                    console.error(err);
                    return res.serveError(err);
                }
                
                // If password DO NOT match
                if (!valid) {
                    var invalidPasswordEmail = [{name: 'invalidPasswordEmail', message: 'Invalid combination of Email / Password'}];
                   
                    req.session.flash = {err: invalidPasswordEmail};
                    return res.redirect('/login');
                }
                
                // Everything is OK begin Authentification
                req.session.user = user;
                
                res.redirect('/dashboard');
            });
        });
    },
    
    'loginFromPhone' : function(req, res) {
	console.log("authentication");
	console.log(req.param['user_key']);
	console.log(req.param('user_key'));

        Key.findOne().where({id: "req.headers.user_key"}).exec(function(err, user) {
            sails.session.user = user;
            if (!user) {
                return res(null, false);
            }
            return res(null, true);
        });
    },

    'logout': function(req, res) {
        
        req.session.destroy();
        res.redirect('/login');
    }
};

