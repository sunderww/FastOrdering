var request = require('supertest');
  var server = request.agent('http://localhost:4343');
var ID = null;
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

function listOption() {
	return function(done) {
		   server
			.post('/option/read')
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
describe('--> Option <--', function() {
 it('login', loginUser());
it('Create a Option', function (done) {
				server
	              .post('/option/create')
	              .send({
	               name:"Saignan"
	               })
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List Options', listOption());
		
		it('Try API route Option', function(done){
 			server
			.post('/option?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0)
		            assert(res[0].name != undefined, "Option creation failed");
	            onResponse(err, res,done);
			});
		});

		it('Update a Option', function (done) {
	           server
	              .post('/Option/update')
	              .send({id:ID.id,
	               name:"Saignant",
	               })
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a Option', function (done) {
	           server
	              .post('/Option/delete')
	              .send({id:ID.id})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});