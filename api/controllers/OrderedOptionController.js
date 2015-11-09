/**
 * @class OrderedOptionController
 *
 * @description :: Server-side logic for managing Options
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {
  /**
   * `OptionController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
        Option.find({id: req.param("id")} ,function(err, doc) {
          return res.send(doc);
      });
    } else {
        OrderedOption.find( function(err, doc) {
	      return res.json({elements: doc});
      });
    }
  },

 delete: function(id) {
      OrderedOption.destroy({}).exec(function(res, doc) {
          return res.ok("ok");
      });

    },

    read_lucas: function (req, res) {
    
      OrderedOption.find( function(err, doc) {
        return res.json(doc);
      });
  },
};

