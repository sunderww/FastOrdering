var request = require('supertest');

describe('--> DishCategory <--', function() {
it('Create a DishCategory', function (done) {
				request(sails.hooks.http.app)
	              .post('/DishCategory/create')
	              .send({
	              	id:"572f78e6937726dc7abffff3",
	               name:"Entree"})
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List DishCategories', function (done) {
		   request(sails.hooks.http.app)
			.post('/DishCategory/read')
			.send()
			.expect(200)
			.end(function (err, res) {
			    done(err);
			});
		});
		
		it('Update a DishCategory', function (done) {
	           request(sails.hooks.http.app)
	              .post('/DishCategory/update')
	              .send({id:"572f78e6937726dc7abffff3", name:"Entree/Plat"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a DishCategory', function (done) {
	           request(sails.hooks.http.app)
	              .post('/DishCategory/delete')
	              .send({id:"572f78e6937726dc7abffff3"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
