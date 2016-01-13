var Sails = require('sails'), sails;
var request = require('supertest');
var server = request.agent('http://localhost:4343');

  before(function (done) {
      Sails.lift({
          log: {
              level: 'silent'
          },
          port: 4343,
          hooks: {
              grunt: false,
          },
      }, function (err, sails) {
      done(err, sails);
      });
  // it('Login', function(done) {
  //      Agent
  //           .post('/login')
  //           .send({email:'toto@toto.com', password:'tototo'})
  //           .expect("Location", "/dashboard")
  //           .end(function(err, res){
  //            Agent.saveCookies(res);
  //             done(err);
  //             console.log()
  //           });
  // }); 
  });


module.exports = {
loginUser: function() {
    return function(done) {

         server
            .post('/login')
            .send({email:'toto@toto.com', password:'tototo'})
            .expect("Location", "/dashboard")
            .end(function(err, res){
              done(err);
            });
        function onResponse(err, res) {
           if (err) return done(err);
           return done();
        }
          };

}
};