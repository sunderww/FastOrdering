var request = require('supertest');

describe('--> OptionCategory <--', function() {
it('Create a OptionCategory', function (done) {
				request(sails.hooks.http.app)
	              .post('/OptionCategory/create')
	              .send({
	               name:"UnitTest",
	               })
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List OptionCategories', function (done) {
		   request(sails.hooks.http.app)
			.post('/OptionCategory/read')
			.send()
			.expect(200)
			.end(function (err, res) {
			    done(err);
			});
		});
		
		it('Update a OptionCategory', function (done) {
	           request(sails.hooks.http.app)
	              .post('/OptionCategory/update')
	              .send({id:"572f78e6937726dc7abffff3",
	               name:"UnitTest2",
	               })
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a OptionCategory', function (done) {
	           request(sails.hooks.http.app)
	              .post('/OptionCategory/delete')
	              .send({id:"572f78e6937726dc7abffff3"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
