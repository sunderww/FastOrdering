var request = require('supertest');

describe('--> Menu <--', function() {
it('Create a Menu', function (done) {
				request(sails.hooks.http.app)
	              .post('/Menu/create')
	              .send({
	              	id:"572f78e6937726dc7abffff3",
	               name:"TestUnit",
	               price:2000})
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('List Menus', function (done) {
		   request(sails.hooks.http.app)
			.post('/Menu/read')
			.send()
			.expect(200)
			.end(function (err, res) {
			    done(err);
			});
		});
		
		it('Update a Menu', function (done) {
	           request(sails.hooks.http.app)
	              .post('/Menu/update')
	              .send({id:"572f78e6937726dc7abffff3",
	               menu_name:"TestUnit2",
	               price:12000})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
		it('Delete a Menu', function (done) {
	           request(sails.hooks.http.app)
	              .post('/Menu/delete')
	              .send({id:"572f78e6937726dc7abffff3"})
	              .expect(302)
	              .end(function (err, res) {
	                  done(err);
	            });
		});
});
