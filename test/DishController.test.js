var request = require('supertest');
var assert = require('assert');
var Sails = require('sails');
var superagent = require('superagent');
var agent;

var app;
var Cookies;
var ID;
describe('--> Login <--', function() {
	before(function (done) {
	    Sails.lift({
	        log: {
	            level: 'silent'
	        },
	        hooks: {
	            grunt: false,
	        },
	    }, function (err, sails) {
			done(err, sails);
			agent = request.agent(sails.hooks.http.app);
			Sails = sails;
	    });
	});
	it('Login', function(done) {
		   agent
		        .post('/login')
		        .send({email:'toto@toto.com', password:'tototo'})
		        .expect(302)
		        .end(function(err, res){
				  agent.saveCookies(res);
		      		done(err);
		        });
	});	
});
describe('--> Dish <--', function() {

it('Create a Dish', function (done) {
				agent
	              .post('/dish/create')
	              .send({
	              	id:"572f78e6937726dc7abffff3",
	               name:"Tarte tatin", price:1000, categories_ids:"12", optioncategories_ids:"12"})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List Dishs', function (done) {
		    request(sails.hooks.http.app)
			.post('/elements')
			.send()
			.expect(200)
			.end(function (err, res) {
			    if(err) return done(err);
			    done();
			});
		});
		
		it('Update a dish', function (done) {
	            request(sails.hooks.http.app)
	              .post('/dish/update')
	              .send({id:"572f78e6937726dc7abffff3", name:"Tarte tatins", price:200, categories_ids:11, optioncategories_ids:11})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		// it('Delete a dish', function (done) {
	 //            request(sails.hooks.http.app)
	 //              .post('/dish/delete')
	 //              .send({id:"572f78e6937726dc7abffff3"})
	 //              .expect(302)
	 //              .end(function (err, res) {
	 //                  done(err);
	 //            });
		// });
});
