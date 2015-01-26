var request = require('supertest');
var assert = require('assert');
var Sails = require('sails');
var app;

describe('OrderController', function() {
    describe('#Order()', function() {
	it('Order list', function (done) {
	    request(sails.hooks.http.app)
		.post('/order/read')
		.send()
		.expect(200)
		.end(function (err, res) {
		    if(err) return done(err);
		    done();
		});
	});
//	it('create an element', function (done) {
//            request(sails.hooks.http.app)
//              .post('/Order/create')
//              .send({:"dessert"})
//              .expect(200)
//              .end(function (err, res) {
//		  if(err) return done(err);
//                  done();
//           });
//	});
    });
});