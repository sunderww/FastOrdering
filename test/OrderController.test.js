var request = require('supertest');
var assert = require('assert');
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

function listOrder(display) {
	return function(done) {
		   server
			.post('/orders')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
		        ID = res[0];
	            onResponse(err, res,done);
			});
	};
}

function onResponse(err, res, done) {
	if (err) return done(err);
    	return done();
};

describe('--> Order <--', function() {
 	it('login', loginUser());
	it('Create an Order', function (done) {
		server
			.post('/order/create')
			.send({numTable:"1", dinerNumber:"2", comments:"comment"})
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
		        ID = res.ret;
	            onResponse(err, res,done); 
			});
	});
	it('List Orders', listOrder());
		
		it('Try API route orders', function(done){
 			server
			.post('/orders')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
		            assert(res[0].numOrder != undefined, "order creation failed");
		            assert(res[0].numTable != undefined, "order creation failed");
		            assert(res[0].numPA != undefined, "order creation failed");
		            assert(res[0].hour != undefined, "order creation failed");
		            assert(res[0].date != undefined, "order creation failed");
		        }
	            onResponse(err, res,done);
			});
		});
		it('Try API route order', function(done){
 			server
			.post('/order?restaurant=56a9e447aa963d4f2a896fd2')
			.send()
			.expect(200)
			.end(function (err, res) {
	            var res = JSON.parse(res.text);
	            if (res.length > 0) {
		            assert(res[0].waiter_id != undefined, "order creation failed");
		            assert(res[0].dinerNumber != undefined, "order creation failed");
		            assert(res[0].table_id != undefined, "order creation failed");
		            assert(res[0].date != undefined, "order creation failed");
		            assert(res[0].time != undefined, "order creation failed");
		            assert(res[0].status != undefined, "order creation failed");
	            }
	            onResponse(err, res,done);
			});
		});
	it('Delete an Order', function (done) {
		server
			.post('/order/delete')
			.send({id:ID.numOrder})
			.expect(200)
			.end(function (err, res) {
	            onResponse(err, res,done);
			});
	});	
	it('Update an Order', function (done) {
		server
			.post('/order/create')
			.send({numOrder:ID.numOrder, numTable:"22", dinerNumber:"2",comments:"pas trop de poivre"})
			.expect(200)
			.end(function (err, res) {
	            onResponse(err, res,done); 
			});
	});	
});