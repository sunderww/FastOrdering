var request = require('supertest');
  var server = request.agent('http://localhost:4343');
var assert = require('assert');
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

function listDishCategory(display) {
	return function(done) {
		   server
			.post('/dishcategory/read')
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

describe('--> DishCategory <--', function() {
 it('login', loginUser());
it('Create a DishCategory', function (done) {
				
				server.post('/DishCategory/create')
				.send({name:"Plats_carte", real_name:"Plats"})
				.expect(200)
				.end(function (err, res) {
	            	onResponse(err, res,done);
	            });
		});
		it('Fail DishCategory creation', function (done) {
	              server.post('/DishCategory/create')
	              .send({name:""})
	              .expect(200)
	              .end(function (err, res) {
	                var res = JSON.parse(JSON.stringify(res.text));
	                assert(res.indexOf("alert-danger") > 0, "DishCategory creation failed");
	                done(err);
	            });
		});
		it('List DishCategories', listDishCategory());
		it('Try API route DishCategory', function(done){
 			server
			.post('/DishCategory?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
		            assert(res[0].name != undefined, "DishCategory creation failed");
		            assert(res[0].real_name != undefined, "DishCategory creation failed");
	    		}
	            onResponse(err, res,done);
			});
		});

		it('Update a DishCategory', function (done) {
	               server.post('/DishCategory/update')
	              .send({id:ID.id,real_name:ID.real_name, name:"Entree/Plat"})
	              .expect(302)
	              .end(function (err, res) {
	   	                done(err);
	            });
		});
		it('Delete a DishCategory', function (done) {
	           server
	              .post('/DishCategory/delete')
	              .send({id:ID.id})
	              .expect(302)
	              .end(function (err, res) {
	            	onResponse(err, res,done);
	            });
		});
});