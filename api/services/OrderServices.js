/**
 * OrderServices.js
 *
 * this file contains the function about key.
 */
var Promise = require('q');
module.exports = {
// {
//     "numOrder": "1",
//     "numTable": "7",
//     "numPA": "2",
//     "date": "01/01/2001",
//     "hour": "12h12",
//     "globalComment": "blablabla"
//     "order": [
//         {
//             "menuId": "2",
//             "content": [
//                 {
//                     "id": "id_dish",
//                     "qty": "2",
//                     "comment": "blabla",
//                     "status": "0",
// 		    "options": [
//                         {
//                             "id": "idsaignant",
//                             "qty": "2"
//                         },
//                         {
//                             "id": "id33cl",
//                             "qty": "2"
//                         }
//                     ]
// 		}
//             ],
//         }
//     ]
// }
	getOneOrder: function(order_id, cb){
		var ret = "toto";

			Promise.all([
			Order.findOne({id:order_id}),
			OrderedDish.find({order_id: order_id})
		])
		.spread(function(order, ordered){
			ordered.forEach(function(entry) {
				entry.qty = entry.quantity;
				entry.id = entry.dish_id
			});
				ret = {
				'numOrder' : order.id,
				'numTable' : order.table_id,
				'date' : order.date,
				'time' : order.time,
				'globalComment': order.comments,
				'order' : [{
					"menuId" : ordered[0].menu_id,
					"content": ordered
				}]
			};
		}).catch(function(err){
			console.log(err);
		})
		.done(function(){
			return cb(ret);
		});
	}
}
 // [ { order_id: '55f0b125bcd880f22267d8f2',
 //    dish_id: '572f78d9937726dc7ab8f8f2',
 //    quantity: 1,
 //    comment: 'ok, chaud2',
 //    menu_id: '572abe8049bb4c97702057db',
 //    status: 'ordered',
 //    createdAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //    updatedAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //    id: '55f0b125bcd880f22267d8f3' },
 //  { order_id: '55f0b125bcd880f22267d8f2',
 //    dish_id: '572f78d9937726dc7ab8f8f2',
 //    quantity: 1,
 //    comment: 'ok, chaud2',
 //    menu_id: '572abe8049bb4c97702057db',
 //    status: 'ordered',
 //    createdAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //    updatedAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //    id: '55f0b125bcd880f22267d8f4' } ]

 // table_id: '1',
 //  dinerNumber: 2,
 //  comments: 'commande avec menu delice',
 //  date: '10/09/2015',
 //  time: '12:22',
 //  status: 'ordered',
 //  createdAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //  updatedAt: Thu Sep 10 2015 00:22:29 GMT+0200 (CEST),
 //  id: '55f0b125bcd880f22267d8f2' }
