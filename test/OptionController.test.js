var request = require('supertest');

describe('--> Option <--', function() {
it('Create a Option', function (done) {
				request(sails.hooks.http.app)
	              .post('/Option/create')
	              .send({
	               name:"UnitTest",
	               optioncategories_ids:"55cccc54f80d3658724d6f7f"
	               })
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List Options', function (done) {
		   request(sails.hooks.http.app)
			.post('/Option/read')
			.send()
			.expect(200)
			.end(function (err, res) {
			    done(err);
			});
		});
		
		it('Update a Option', function (done) {
	           request(sails.hooks.http.app)
	              .post('/Option/update')
	              .send({id:"572f78e6937726dc7abffff3",
	               name:"UnitTest2",
	               })
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a Option', function (done) {
	           request(sails.hooks.http.app)
	              .post('/Option/delete')
	              .send({id:"572f78e6937726dc7abffff3"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
