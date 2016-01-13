// var request = require('supertest');

// describe('--> MenuComposition <--', function() {
// it('Create a MenuComposition', function (done) {

// 				  request(sails.hooks.http.app).post('/MenuComposition/create')
// 	              .send({
// 	              	id:"572f78e6937726dc7abffff3",
// 	                name:"Entree", 
// 	                categories_ids:"22", 
// 	                menu_id:"12"})
// 	              .end(function (err, res) {
// 	                  done(err);
// 	            });
// 		});
// 		it('List MenuCompositions', function (done) {
// 		    request(sails.hooks.http.app)
// 			.post('/MenuComposition')
// 			.send()
// 			.expect(200)
// 			.end(function (err, res) {
// 			    done(err);
// 			});
// 		});
		
// 		// it('Update a MenuComposition', function (done) {
// 	 //            request(sails.hooks.http.app)
// 	 //              .post('/MenuComposition/update')
// 	 //              .send({id:"572f78e6937726dc7abffff3", name:"Entree/Plat",categories_ids:"22", menu_id:"12"})
// 	 //              .expect(302)
// 	 //              .end(function (err, res) {
// 	 //                  done(err);
// 	 //            });
// 		// });
// 		it('Delete a MenuComposition', function (done) {
// 	            request(sails.hooks.http.app)
// 	              .post('/menucomposition/delete/')
// 	              .send({id:"572f78e6937726dc7abffff3"})
// 	              .expect(200)
// 	              .end(function (err, res) {
// 	                  done(err);
// 	            });
// 		});
// });
