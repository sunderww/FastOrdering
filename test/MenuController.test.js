var request = require('supertest');
var ID = null;
var server = request.agent('http://localhost:4343');
var assert = require('assert');


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

function listMenu(display) {
	return function(done) {
		   server
			.post('/menu/read')
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

describe('--> Menu <--', function() {
 it('login', loginUser());
it('Create a Menu', function (done) {
				server
	              .post('/menu/create')
	              .send({
	               menu_name:"Gour",
	               price:12})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Fail Menu creation', function (done) {
	              server.post('/menu/create')
	              .send({name:""})
	              .expect(200)
	              .end(function (err, res) {
	                var res = JSON.parse(JSON.stringify(res.text));
	                assert(res.indexOf("alert-danger") > 0, "Menu creation failed");
	                done(err);
	            });
		});	

		it('List Menus',listMenu());
		it('Try API route Menu', function(done){
 			server
			.post('/Menu?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
	            	assert(res[0].name != undefined, "Menu creation failed");
	            	assert(res[0].price != undefined, "Menu creation failed");
	            }
	            onResponse(err, res,done);
			});
		});
		
		it('Update a Menu', function (done) {
	           server
	              .post('/Menu/update')
	              .send({id:ID.id,
	               name:"Gourmet",
	               price:12})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a Menu', function (done) {
	           server
	              .post('/Menu/delete')
	              .send({id:ID.id})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
