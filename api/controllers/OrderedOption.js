/**
 * OrderedOption
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
    read_lucas: function (req, res) {
    
      OrderedOption.find( function(err, doc) {
        return res.json(doc);
      });
  },
};

