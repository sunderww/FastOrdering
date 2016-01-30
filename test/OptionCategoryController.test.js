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

function listOptionCategory(display) {
	return function(done) {
		   server
			.post('/optioncategory/read')
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

describe('--> OptionCategory <--', function() {
 it('login', loginUser());
		it('Create a OptionCategory', function (done) {
	              server.post('/OptionCategory/create')
	              .send({name:"Cuison"})
	              .expect(200)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Fail OptionCategory creation', function (done) {
	              server.post('/OptionCategory/create')
	              .send({name:""})
	              .expect(200)
	              .end(function (err, res) {
	                var res = JSON.parse(JSON.stringify(res.text));
	                assert(res.indexOf("alert-danger") > 0, "OptionCategory creation failed");
	                done(err);
	            });
		});
		it('List OptionCategories', listOptionCategory());
		it('Try API route OptionCategory', function(done){
 			server
			.post('/OptionCategory?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
	           		assert(res[0].name != undefined, "OptionCategory creation failed");
	            	assert(res[0].option_ids != undefined, "OptionCategory creation failed");
	            }
	            onResponse(err, res,done);
			});
		});
		it('Update a OptionCategory', function (done) {
	           server.post('/OptionCategory/update')
	              .send({id:ID.id,
	               name:"Cuisson",
	               })
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a OptionCategory', function (done) {
	           server.post('/OptionCategory/delete')
	              .send({id:ID.id})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});