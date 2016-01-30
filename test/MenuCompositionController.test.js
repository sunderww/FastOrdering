var request = require('supertest');
var assert = require('assert');
var ID = null;
var server = request.agent('http://localhost:4343');

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

function listMenuComposition(display) {
	return function(done) {
		   server
			.post('/MenuComposition/read')
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

describe('--> MenuComposition <--', function() {
 it('login', loginUser());
it('Create a MenuComposition', function (done) {

				 server.post('/MenuComposition/create')
	              .send({
	                name:"Dess", 
	                categories_ids:"56aa9db3225d8d7c2e82c212", 
	                menu_id:"56a159eafe26ab508f2e6f8f",
	            	position:"1"})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List MenuCompositions',listMenuComposition());
		it('Try API route MenuCompositions', function(done){
 			server
			.post('/MenuComposition?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
	            	assert(res[0].name != undefined, "MenuComposition creation failed");
	            	assert(res[0].position != undefined, "MenuComposition creation failed");
	            	assert(res[0].categories_ids != undefined, "MenuComposition creation failed");
	            }
	            onResponse(err, res,done);
			});
		});

		it('Delete a MenuComposition', function (done) {
	            server
	              .post('/menucomposition/delete/')
	              .send({id:ID.id})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
