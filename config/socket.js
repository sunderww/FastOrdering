/**
 * WebSocket Server Settings
 * (sails.config.sockets)
 *
 * These settings provide transparent access to the options for Sails'
 * encapsulated WebSocket server, as well as some additional Sails-specific
 * configuration layered on top.
 *
 * For more information on sockets configuration, including advanced config options, see:
 * http://sailsjs.org/#/documentation/reference/sails.config/sails.config.sockets.html
 */



 module.exports.sockets = {



  /***************************************************************************
  *                                                                          *
  * This custom onConnect function will be run each time AFTER a new socket  *
  * connects (To control whether a socket is allowed to connect, check out   *
  * `authorization` config.) Keep in mind that Sails' RESTful simulation for *
  * sockets mixes in socket.io events for your routes and blueprints         *
  * automatically.                                                           *
  *                                                                          *
  ***************************************************************************/
  onConnect: function(session, socket) {
    console.log("Connect");
      // console.log(socket);
      // console.log(socket.id);
      socket.on('send_order', function(json, cb) {
     console.log("send_order");
     try {
      var json = JSON.parse(json);
    }catch(e){

    }
	console.log(json);
	if (json.numOrder != undefined) {
//	      sails.controllers.Order.delete(json.numOrder);
Order.destroy({id:json.numOrder}).exec(function(err, doc) {
//	    return res.ok("ok");
	});
	    id = json.numOrder;
	console.log("EDIT");
	console.log(id);
	}
	else 
	    id = null;
     Order.create({
	    id:id,
      table_id:json.numTable,
      dinerNumber:json.numPA,
      comments: json.globalComment
    }).exec(function(err,model){
	if (err)
	    return cb(err);
      var arr = [];

      for (var i = 0;json['order'][0].content[i]; i++) {

	  console.log(json['order'][0].content[i]);
        OrderedDish.create({
          order_id:model.id,
          dish_id:json['order'][0].content[i].id,
          quantity:json['order'][0].content[i].qty,
          comment:json['order'][0].content[i].comment,
          menu_id:json['order'][0].menuId,
          options:json['order'][0].content[i].options

        }).exec(function(err,model){
          console.log(err);
	    return cb(err);
        });
        var arr = {numOrder: model.id, numTable: json.numTable, numPA: json.numPA, date:model.date, hour:model.time};
      console.log(arr);
    socket.emit('receive_order', arr);
	  cb(arr);
      }


  });
  });

      socket.on('authentication', function(json){
	  console.log("Beau gosse du 36");
      });

    socket.on('get_order', function(json, cb){
//      console.log("get_order" + json.order);
      OrderServices.getOneOrder(json.order, function (result) {
  //      console.log(result);
        cb(result);
      });
    });

    socket.on('get_last_orders', function(cb){
      OrderServices.getLastOrders(10, function (result) {
    //    console.log(result);
          cb({"orders": result});
      });
    });

  },


  /***************************************************************************
  *                                                                          *
  * This custom onDisconnect function will be run each time a socket         *
  * disconnects                                                              *
  *                                                                          *
  ***************************************************************************/
     afterDisconnect: function(session, socket, cb) {
	 cb();
    // By default: do nothing.
  },


  /***************************************************************************
  *                                                                          *
  * `transports`                                                             *
  *                                                                          *
  * A array of allowed transport methods which the clients will try to use.  *
  * The flashsocket transport is disabled by default You can enable          *
  * flashsockets by adding 'flashsocket' to this list:                       *
  *                                                                          *
  ***************************************************************************/
  // transports: [
//   'websocket',
  // 'htmlfile',
  // 'xhr-polling',
  // 'jsonp-polling'
//   ],

  /***************************************************************************
  *                                                                          *
  * Use this option to set the datastore socket.io will use to manage        *
  * rooms/sockets/subscriptions: default: memory                             *
  *                                                                          *
  ***************************************************************************/

  // adapter: 'memory',

  /***************************************************************************
  *                                                                          *
  * Node.js (and consequently Sails.js) apps scale horizontally. It's a      *
  * powerful, efficient approach, but it involves a tiny bit of planning. At *
  * scale, you'll want to be able to copy your app onto multiple Sails.js    *
  * servers and throw them behind a load balancer.                           *
  *                                                                          *
  * One of the big challenges of scaling an application is that these sorts  *
  * of clustered deployments cannot share memory, since they are on          *
  * physically different machines. On top of that, there is no guarantee     *
  * that a user will "stick" with the same server between requests (whether  *
  * HTTP or sockets), since the load balancer will route each request to the *
  * Sails server with the most available resources. However that means that  *
  * all room/pubsub/socket processing and shared memory has to be offloaded  *
  * to a shared, remote messaging queue (usually Redis)                      *
  *                                                                          *
  * Luckily, Socket.io (and consequently Sails.js) apps support Redis for    *
  * sockets by default. To enable a remote redis pubsub server, uncomment    *
  * the config below.                                                        *
  *                                                                          *
  * Worth mentioning is that, if `adapter` config is `redis`, but host/port  *
  * is left unset, Sails will try to connect to redis running on localhost   *
  * via port 6379                                                            *
  *                                                                          *
  ***************************************************************************/

  // adapter: 'redis',
  // host: '127.0.0.1',
  // port: 6379,
  // db: 'sails',
  // pass: '<redis auth password>'


  /***************************************************************************
  *                                                                          *
  * `authorization`                                                          *
  *                                                                          *
  * Global authorization for Socket.IO access, this is called when the       *
  * initial handshake is performed with the server.                          *
  *                                                                          *
  * By default (`authorization: false`), when a socket tries to connect,     *
  * Sails allows it, every time. If no valid cookie was sent, a temporary    *
  * session will be created for the connecting socket.                       *
  *                                                                          *
  * If `authorization: true`, before allowing a connection, Sails verifies   *
  * that a valid cookie was sent with the upgrade request. If the cookie     *
  * doesn't match any known user session, a new user session is created for  *
  * it. (In most cases, the user would already have a cookie since they      *
  * loaded the socket.io client and the initial HTML page.)                  *
  *                                                                          *
  * However, in the case of cross-domain requests, it is possible to receive *
  * a connection upgrade request WITHOUT A COOKIE (for certain transports)   *
  * In this case, there is no way to keep track of the requesting user       *
  * between requests, since there is no identifying information to link      *
  * him/her with a session. The sails.io.js client solves this by connecting *
  * to a CORS endpoint first to get a 3rd party cookie (fortunately this     *
  * works, even in Safari), then opening the connection.                     *
  *                                                                          *
  * You can also pass along a ?cookie query parameter to the upgrade url,    *
  * which Sails will use in the absense of a proper cookie e.g. (when        *
  * connection from the client):                                             *
  * io.connect('http://localhost:1337?cookie=smokeybear')                    *
  *                                                                          *
  * (Un)fortunately, the user's cookie is (should!) not accessible in        *
  * client-side js. Using HTTP-only cookies is crucial for your app's        *
  * security. Primarily because of this situation, as well as a handful of   *
  * other advanced use cases, Sails allows you to override the authorization *
  * behavior with your own custom logic by specifying a function, e.g:       *
  *                                                                          *
  *    authorization: function authSocketConnectionAttempt(reqObj, cb) {     *
  *                                                                          *
  *        // Any data saved in `handshake` is available in subsequent       *
  *        requests from this as `req.socket.handshake.*`                    *
  *                                                                          *
  *        // to allow the connection, call `cb(null, true)`                 *
  *        // to prevent the connection, call `cb(null, false)`              *
  *        // to report an error, call `cb(err)`                             *
  *     }                                                                    *
  *                                                                          *
  ***************************************************************************/

// beforeConnect: true,
     beforeConnect: function (reqObj, cb) {     
           // Any data saved in `handshake` is available in subsequent       
           //requests from this as `req.socket.handshake.*`                    
	console.log("Hello");
     Key.findOne().where({id: reqObj.headers.user_key}).exec(function(err, user) {
       sails.session.user = user;
       if (!user) {
         console.log("bad");
         socket.emit('authentication', {answer: false});
         return cb(null, false);
       }
       console.log("good");
       socket.emit('authentication', {answer: true});
       return cb(null, true);
     });                                                                       
          // to allow the connection, call `cb(null, true)`                 
          // to prevent the connection, call `cb(null, false)`              
          // to report an error, call `cb(err)`                             
   },

  /***************************************************************************
  *                                                                          *
  * Whether to run code which supports legacy usage for connected sockets    *
  * running the v0.9 version of the socket client SDK (i.e. sails.io.js).    *
  * Disabled in newly generated projects, but enabled as an implicit default *
  * (i.e. legacy usage/v0.9 clients be supported if this property is set to  *
  * true, but also if it is removed from this configuration file or set to   *
  * `undefined`)                                                             *
  *                                                                          *
  ***************************************************************************/

  // 'backwardsCompatibilityFor0.9SocketClients': false,

  /***************************************************************************
  *                                                                          *
  * Whether to expose a 'get /__getcookie' route with CORS support that sets *
  * a cookie (this is used by the sails.io.js socket client to get access to *
  * a 3rd party cookie and to enable sessions).                              *
  *                                                                          *
  * Warning: Currently in this scenario, CORS settings apply to interpreted  *
  * requests sent via a socket.io connection that used this cookie to        *
  * connect, even for non-browser clients! (e.g. iOS apps, toasters, node.js *
  * unit tests)                                                              *
  *                                                                          *
  ***************************************************************************/

  // grant3rdPartyCookie: true,

  /***************************************************************************
  *                                                                          *
  * Match string representing the origins that are allowed to connect to the *
  * Socket.IO server                                                         *
  *                                                                          *
  ***************************************************************************/

  // origins: '*:*',

};
