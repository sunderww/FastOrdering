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
  });