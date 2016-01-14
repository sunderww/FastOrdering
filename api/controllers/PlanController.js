/**
 * PlanController
 *
 * @module      :: Controller
 * @description	:: A set of functions called `actions`.
 *
 *                 Actions contain code telling Sails how to respond to a certain type of request.
 *                 (i.e. do stuff, then send some JSON, show an HTML page, or redirect to another URL)
 *
 *                 You can configure the blueprint URLs which trigger these actions (`config/controllers.js`)
 *                 and/or override them with custom routes (`config/routes.js`)
 *
 *                 NOTE: The code you write here supports both HTTP and Socket.io automatically.
 *
 * @docs        :: http://sailsjs.org/#!documentation/controllers
 */

module.exports = {

  index: function (req, res) {
      Plan.find(function foundPlan(err, plans) {
      if (err) return next(err);
      Table.find(function foundTable(err,table) {
        if (err) return next(err);
        res.view({
          plans_collection: plans,
          tables_collection: table
        });
      })
      
    });
  },

  create: function (req, res) {
    console.log(req.body);
    var nreq = JSON.parse(req.param("json"));
    console.log(nreq);
    var shapes = nreq.position;
    Plan.create({
      name:nreq.title,
      dimX:nreq.dimX,
      dimY:nreq.dimY,
      numShapes:nreq.numShapes
    }).exec(function(err,model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
      else {
      	for (var i = 0;i < shapes.length; i++) {
    			Table.create({
    		      name:shapes[i].name,
    		      plan:shapes[i].plan,
    		      waiters:shapes[i].waiters,
    		      posx:shapes[i].posx,
    		      posy:shapes[i].posy
  		    }).exec(function(err,model){
  		      if (err) {
  		        return res.json({
  		          message: err.ValidationError
  		        });
  		      }
  		        console.log(req.param('name') + " (Table) has been updated");       
  		    });
      	}
      	console.log(err);
      	console.log(req.param("title") + " a été sauvegardé");
        res.redirect('/plan');
      }
    });
  },

  /**
   * `PlanController.destroy()`
   */

  destroyAll: function (req, res) {
  	Plan.destroy({}).exec(function deleteCB(err){
	  console.log('Plan collection has been deleted');
  	});
  	Table.destroy({}).exec(function deleteCB(err){
        console.log('Table collection has been deleted');
      });
    res.redirect('/plan');
  },

  /**
   * `PlanController.update()`
   */


  update: function (req, res) {
    console.log(req.body);

    var nreq = JSON.parse(req.param("json"));

    console.log(nreq);

    var shapes = nreq.position;
    Plan.destroy({name:nreq.old_title}).exec(function deletePR(err) {
    	if (err) {
    		console.log(err);
    	} else {
			console.log('Plan ' + nreq.old_title + " have been cleansed");
		    Plan.create({
			    name:nreq.title,
			    dimX:nreq.dimX,
			    dimY:nreq.dimY,
			    numShapes:nreq.numShapes
		    }).exec(
		    	function(err, found) {
			       if (err) {
			       	console.log(err);
			        return res.json({
			          message: err
			        });
		        } else {
	       			Table.destroy({plan:nreq.old_title}).exec(function deleteTR(err){
	       				if (err) {
	       					console.log(err);
	       				} else {
	       					console.log('Tables of ' + nreq.old_title + " have been cleansed");
				      		for (var i = 0;i < shapes.length; i++) {
					      		console.log("Tables updating");
								Table.create({
								    name:shapes[i].name,
								    plan:shapes[i].plan,
								    waiters:shapes[i].waiters,
								    posx:shapes[i].posx,
								    posy:shapes[i].posy
							    }).exec(function(err,model){
							    	if (err) {
								        return res.json({
								        	message: "error"
								        });
							    	}
							        console.log(req.param('name') + " (Table) has been created");       
							    });
				      		}
				      	console.log(err);
				      	console.log(req.param("title") + " a été sauvegardé");
				        Plan.find( function(err, doc) {
							Table.find(function foundTable(err,table) {
				  			if (err) return next(err);
                  res.redirect('/plan');
				  		})
				  		});
	       				}
					});
	  		    } 
		    });
    	}
	})
  },

  /**
   * `PlanController.read()`
   */
  read: function (req, res) {
    if (req.param("name")) {
      Plan.find({name: req.param("name")}, function(err, doc) {
        return res.send(JSON.stringify(doc));
      });
    } else {
      Plan.find( function(err, doc) {
        return res.send(JSON.stringify(doc));
      });
    }
  },

  readAll: function (req, res) {
   Plan.find( function(err, doc) {
		return res.json({elements: doc});
  	});
  },

  findOne: function(req, res) {
  	console.log(req.body);
  	if (req.param("json")) {
  		Plan.find({name: req.param("json")}, function(err, doc) {
			Table.find(function foundTable(err,table) {
  			if (err) return next(err);
          res.redirect('/plan');
  		})
  		});
  	} else {
  		Plan.find( function(err, doc) {
			Table.find(function foundTable(err,table) {
  			if (err) return next(err);
          res.redirect('/plan');
  		})
  		});
  	}
  },

};
