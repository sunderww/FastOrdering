/**
 * BookingController
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
    
  'new':function(req, res){
  	res.locals.flash = _.clone(req.session.flash);
  	res.view();
  	req.session.flash = {};
  },

  create: function(req, res){

    var datetime = String((sails.moment(req.param('date') + " " + req.param('time'), "YYYY-MM-DD HH:mm:ss").format("YYYY-MM-DD HH:mm:ss")));
    var params = { date:datetime, name:req.param('name'), nb_persons:req.param('nb_persons'), restaurant_id:req.param('restaurant_id') };

  	Booking.create(params, function bookingCreated(err, booking) {
  		if (err) {
  			console.log(err);
  			req.session.flash = {
  				err: err
  			}


  			return res.redirect('/booking/new');
  		}

  		res.json(booking);
	  	req.session.flash = {};
  	});	
  },

  edit: function(req, res){
  	Booking.findOne(req.param('id'), function foundBooking(err, booking) {
  		if (err) 
            return res.serveError(err);
  		if (!booking) 
            return res.notFound();

  		res.view({
  			booking: booking
  		});
  	});
  },

  update: function(req, res){
    
    var datetime = String((sails.moment(req.param('date') + " " + req.param('time'), "YYYY-MM-DD HH:mm:ss").format("YYYY-MM-DD HH:mm:ss")));
    var params = { date:datetime, name:req.param('name'), nb_persons:req.param('nb_persons'), restaurant_id:req.param('restaurant_id') };

      
  	Booking.update(req.param('id'), params, function userUpdated(err) {
  		if (err) {
  			return res.redirect('/booking/edit/' + req.param('id'));
  		}

  		res.redirect('/booking/index');
  	});
  },

  index: function(req, res){
  	Booking.find(function foundBooking(err, bookings) {
  		if (err) 
            return res.serveError(err);

      bookings.forEach(function(entry) {
        entry.date = String(entry.date).substr(4, 20);
        entry.date = String((sails.moment(entry.date, "MMM DD YYYY HH:mm:ss").format(sails.moment.ISO_8601())));
      });

  		res.view({
  			bookings: bookings
  		});
  	});
  },

  /**
   * Overrides for the settings in `config/controllers.js`
   * (specific to BookingController)
   */
  _config: {}

  
};
