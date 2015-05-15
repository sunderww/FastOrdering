var request = require('supertest');
var assert = require('assert');
var Sails = require('sails');
var app;
before(function (done) {
    Sails.lift({
        // configuration for testing purposes
        log: {
            //level: 'silly'
            level: 'silent'
        },
        hooks: {
            grunt: false,
        },
    }, function (err, sails) {
	done(err, sails);
	Sails = sails;
    });
});

describe('DishController', function() {
    describe('#dish()', function() {
	it('list the elements', function (done) {
	    request(sails.hooks.http.app)
		.post('/elements')
		.send()
		.expect(200)
		.end(function (err, res) {
		    if(err) return done(err);

//		    assert.ok(res.body.title);
//		    assert.ok(res.body.body);
//		    assert.equal(res.body.title, postStub.title);
//		    assert.equal(res.body.body, postStub.body);

		    done();
		});
	});
	it('create an element', function (done) {
            request(sails.hooks.http.app)
                .post('/dish/create')
                .send({name:"Glacee", price:7})
                .expect(200)
                .end(function (err, res) {
		    if(err) return done(err);
                    done();
                });
	});
    });
});