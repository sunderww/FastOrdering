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
            
            var emailPasswordRequired = [{name: 'emailPasswordRequired', message: 'Vous devez entrer un Email et Mot de passe.'}];

            req.session.flash = {login: emailPasswordRequired};
            return res.redirect('/login');
        }
        
        // Try to find the email link to the user
        User.findOne({email: req.param('email')}).exec(function findOneEmail(err, user) {
            if (err) {
                console.error(err);
                return res.serverError(err); // TODO : I don't know if it is the best way ??
            }
           
            // If there is NO user
            if (!user) {
                var invalidPasswordEmail = [{name: 'invalidPasswordEmail', message: 'Email ou Mot de passe invalide.'}];
                
                req.session.flash = {login: invalidPasswordEmail};
                return res.redirect('/login');
            }
            
            // Try to match password
            sails.bcrypt.compare(req.param('password'), user.password, function (err, valid){
                if (err) {
                    console.error(err);
                    return res.serverError(err);
                }
                
                // If password DO NOT match
                if (!valid) {
                    var invalidPasswordEmail = [{name: 'invalidPasswordEmail', message: 'Email ou Mot de passe invalide.'}];
                   
                    req.session.flash = {login: invalidPasswordEmail};
                    return res.redirect('/login');
                }
                
                // Everything is OK begin Authentification
                req.session.user = user;
                
                console.log("Connection User --> " + user.email);
                req.session.flash = {};
                res.redirect('/dashboard');
            });
        });
    },

    'logout': function(req, res) {
        
        req.session.destroy();
        res.redirect('/login');
    }
};

