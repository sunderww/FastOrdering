/**
 * TableController
 *
 * @module      :: Controller
 * @description :: Server-side logic for managing Tables
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {

  /**
   * `TableController.create()`
   */
  create: function (req, res) {
    console.log("Trying to save Table");
    Table.create({
      name:req.param("name"),
      waiters:req.param("waiters"),
      dishes:req.param("dishes"),
      posx:req.param("posx"),
      posy:req.param("posy")
    }).exec(function(err,model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
        console.log(req.param('name') + " (Table) has been created");       
    });
  },



  /**
   * `TableController.destroy()`
   */
/*  destroy: function (req, res) {
    Table.destroy({}).exec(function deleteCB(err){
      console.log('Table collection has been deleted');
    });
    return res.json({
      todo: 'table collection flushed'
    });
  },*/

  /**
   * `TableController.update()`
   */
  update: function (req, res) {
    console.log("Trying to update Table");
    Table.update({
      name:req.param("name"),
      waiters:req.param("waiters"),
      dishes:req.param("dishes"),
      posx:req.param("posx"),
      posy:req.param("posy")
    }).exec(function(err,model){
      if (err) {
        return res.json({
          message: err.ValidationError
        });
      }
        console.log(req.param('name') + " (Table) has been updated");       
    });
  },

  /**
   * `TableController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
      Table.find({id: req.param("id")}, function(err, doc) {
        return res.send(doc);
      });
    } else {
      Table.find( function(err, doc) {
        return res.send(doc);
      });
    }
  },

  findByPlan: function (req, res) {
      Table.find({plan: req}, function(err, doc) {
        return res.send(doc);
      });
  },

  findAll: function (req, res) {
    Table.find(function(err, doc) {
      return res.json({elements: doc});
    });
  }

};