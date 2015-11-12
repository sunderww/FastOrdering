var Sails = require('sails'), sails;
var request = require('supertest');
var agent = require('superagent'), Agent;
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
      Agent = request.agent(sails.hooks.http.app);
      });
  it('Login', function(done) {
       Agent
            .post('/login')
            .send({email:'toto@toto.com', password:'tototo'})
            .expect(302)
            .end(function(err, res){
             Agent.saveCookies(res);
              done(err);
            });
  }); 
  });

