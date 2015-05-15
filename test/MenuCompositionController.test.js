var request = require('supertest');
var assert = require('assert');
var Sails = require('sails');
var app;

describe('MenuCompositionController', function() {
    describe('#MenuComposition()', function() {
	it('Menu list', function (done) {
	    request(sails.hooks.http.app)
		.post('/menucomposition/read')
		.send()
		.expect(200)
		.end(function (err, res) {
		    if(err) return done(err);
		    done();
		});
	});
	it('create an element', function (done) {
            request(sails.hooks.http.app)
              .post('/menucomposition/create')
              .send({name:"dessert"})
              .expect(200)
              .end(function (err, res) {
		  if(err) return done(err);
                  done();
            });
	});
    });
});