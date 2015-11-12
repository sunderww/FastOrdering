var request = require('supertest');

describe('--> Dish <--', function() {
it('Create a Dish', function (done) {

				  request(sails.hooks.http.app).post('/dish/create')
	              .send({
	              	id:"572f78e6937726dc7abffff3",
	               name:"Tarte tatin", price:1000, categories_ids:"12", optioncategories_ids:"12"})
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
			    done(err);
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
		it('Delete a dish', function (done) {
	            request(sails.hooks.http.app)
	              .post('/dish/delete')
	              .send({id:"572f78e6937726dc7abffff3"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
