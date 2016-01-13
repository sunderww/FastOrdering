var request = require('supertest');
  var server = request.agent('http://localhost:4343');


function loginUser() {
    return function(done) {

         server
            .post('/login')
            .send({email:'toto@toto.com', password:'tototo'})
            .expect("Location", "/dashboard")
            .end(function(err, res){
              console.log(err);
              done();
            });
      
          };
};
function onResponse(err, res, done) {
	if (err) return done(err);
    	return done();
}

describe('--> DishCategory <--', function() {
 it('login', loginUser());
it('Create a DishCategory', function (done) {
				server.post('/DishCategory/create')
	              .send({name:"toto"})
	              .end(function (err, res) {
	            	onResponse(err, res,done);
	            });
		});
		it('List DishCategories', function (done) {
		   server
			.post('/dishcategory/read')
			.send()
			.expect(200)
			.end(function (err, res) {
	            onResponse(err, res,done);
			});
		});
		
		// it('Update a DishCategory', function (done) {
	 //           request(sails.hooks.http.app)
	 //              .post('/DishCategory/update')
	 //              .send({id:"572f78e6937726dc7abffff3", name:"Entree/Plat"})
	 //              .expect(302)
	 //              .end(function (err, res) {
	 //                  done(err);
	 //            });
		// });
		// it('Delete a DishCategory', function (done) {
	 //           server
	 //              .post('/DishCategory/delete')
	 //              .send({id:"5694dc0bfe21ac89fe9bfad2"})
	 //              .expect(302)
	 //              .end(function (err, res) {
	 //            	onResponse(err, res,done);
	 //            });
		// });
});
