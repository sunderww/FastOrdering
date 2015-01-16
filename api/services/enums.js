/**
 * enums.js
 *
 * this file contains the enums used in FastOrdering
 */


module.exports = {

	DishStatus: {
	  canceled: 1 << 0,
	  ordered: 1 << 1,
	  cooking: 1 << 2,
	  toDeliver: 1 << 3,
	  delivered: 1 << 4
	},

	UserRole: {
	  waiter: 1 << 0,		// can manage dishes, orders and bookings
	  manager: 1 << 1,		// can edit restaurant settings and has waiter and cookers rights

	  admin: 1 << 2			// admin role is not for customers but only for internal administration
	}

};