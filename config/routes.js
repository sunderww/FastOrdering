/**
 * Route Mappings
 * (sails.config.routes)
 *
 * Your routes map URLs to views and controllers.
 *
 * If Sails receives a URL that doesn't match any of the routes below,
 * it will check for matching files (images, scripts, stylesheets, etc.)
 * in your assets directory.  e.g. `http://localhost:1337/images/foo.jpg`
 * might match an image file: `/assets/images/foo.jpg`
 *
 * Finally, if those don't match either, the default 404 handler is triggered.
 * See `api/responses/notFound.js` to adjust your app's 404 logic.
 *
 * Note: Sails doesn't ACTUALLY serve stuff from `assets`-- the default Gruntfile in Sails copies
 * flat files from `assets` to `.tmp/public`.  This allows you to do things like compile LESS or
 * CoffeeScript for the front-end.
 *
 * For more information on configuring custom routes, check out:
 * http://sailsjs.org/#/documentation/concepts/Routes/RouteTargetSyntax.html
 */

module.exports.routes = {

  /***************************************************************************
  *                                                                          *
  * Make the view located at `views/homepage.ejs` (or `views/homepage.jade`, *
  * etc. depending on your default view engine) your home page.              *
  *                                                                          *
  * (Alternatively, remove this and add an `index.html` file in your         *
  * `assets` directory)                                                      *
  *                                                                          *
  ***************************************************************************/

  '/': {
    view: 'homepage'
    //view: 'static/index'
  },
    // Session
    'get /login': 'SessionController.login',
    'post /login': 'SessionController.trylogin',
    '/logout': 'SessionController.logout',
    '/authentication': 'SessionController.loginFromPhone',

    // User
    'get /register': 'UserController.register',
    'post /register': 'UserController.createRestaurant', // Create an Admin User with restaurant
    '/user': 'UserController.index',
    '/user/edit': 'UserController.edit',

    // '/user/': 'UserController.show', TODO
    
    // Key
    // If want restful modify address by /key/:id/action
    '/user/create_key': 'KeyController.create',
    'get /key/:id/activate': 'UserController.activateKey',
    'post /key/:id/activate': 'UserController.createWaiterOrCook',
    
    '/dashboard': 'DashboardController.view',
    '/licence': {
      view: 'licence' 
    },

    // Booking
    '/booking' : 'BookingController.index',
    '/booking/new' : 'BookingController.new',
    '/booking/edit' : 'BookingController.edit',
    '/booking/destroy:id': 'BookingController.destroy',
    
    // Stats
    '/stats' : 'StatsController.index',

    '/elements' : 'DishController.read',
    '/menus' : 'MenuController.read',
    '/kitchen' : 'OrderController.getToday',   

    '/manage_plan' : 'PlanController.index',
    '/compos' : 'MenuCompositionController.read',
    '/alacarte' : 'MenuController.alacarte',
    '/cats' : 'DishCategoryController.read',
    '/options': 'OptionCategoryController.read',
    '/send_order': 'DishController.create',
    '/optioncategory' : 'OptionCategoryController.read_lucas',
    '/orderedoption' : 'OrderedOptionController.read_lucas',
    '/orders' : 'OrderController.read_xavier',
    '/ordereddish' : 'OrderedDishController.read_lucas',
  /***************************************************************************
  *                                                                          *
  * Custom routes here...                                                    *
  *                                                                          *
  *  If a request to a URL doesn't match any of the custom routes above, it  *
  * is matched against Sails route blueprints. See `config/blueprints.js`    *
  * for configuration options and examples.                                  *
  *                                                                          *
  ***************************************************************************/

};
