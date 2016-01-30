var assert = require('assert');
var request = require('supertest');
  var server = request.agent('http://localhost:4343');
var ID = null;

function loginUser() {
    return function(done) {
            server.post('/login')
            .send({email:'test_restaurant@fastordering.fr', password:'tototo'})
            .expect("Location", "/dashboard")
            .end(function(err, res){
              done(err);
            });
    };
}

function listDish(display) {
	return function(done) {
		   server
			.post('/dish/read')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
		        ID = res.elements[0];
	            onResponse(err, res,done);
			});
	};
}

function onResponse(err, res, done) {
	if (err) return done(err);
    	return done();
}

describe('--> Dish <--', function() {
 it('login', loginUser());
		it('Create a Dish', function (done) {

				  server.post('/dish/create')
	              .send({
	               name:"Tate tatin", price:6, categories_ids:"", optioncategories_ids:""})
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Fail Dish creation', function (done) {
	              server.post('/Dish/create')
	              .send({name:""})
	              .expect(200)
	              .end(function (err, res) {
	                var res = JSON.parse(JSON.stringify(res.text));
	                assert(res.indexOf("alert-danger") > 0, "Dish creation failed");
	                done(err);
	            });
		});		
		it('List Dishs', listDish());
		
		it('Try API route Dish', function(done){
 			server
			.post('/dish?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
	            	assert(res[0].name != undefined, "Dish creation failed");
	            	assert(res[0].price != undefined, "Dish creation failed");
	            	assert(res[0].categories_ids != undefined, "Dish creation failed");
	            	assert(res[0].optcats_ids != undefined, "Dish creation failed");
	            }
	            onResponse(err, res,done);
			});
		});

		it('Try API route Dish', function(done){
 			server
			.post('/elements')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
	            	assert(res[0].name != undefined, "Dish creation failed");
	            	assert(res[0].price != undefined, "Dish creation failed");
	            	assert(res[0].categories_ids != undefined, "Dish creation failed");
	            	assert(res[0].options != undefined, "Dish creation failed");
	            }
	            onResponse(err, res,done);
			});
		});

		it('Update a dish', function (done) {
	            server
	              .post('/dish/update')
	              .send({id:ID.id, name:"Tarte tatins", price:6, categories_ids:"", optioncategories_ids:""})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a dish', function (done) {
	            server
	              .post('/dish/delete')
	              .send({id:ID.id})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
