/**
 * OptionCategoryController
 *
 * @description :: Server-side logic for managing Optioncategories
 * @help        :: See http://links.sailsjs.org/docs/controllers
 */

module.exports = {

  /**
   * `OptionCategoryController.create()`
   */
  create: function (req, res) {
    if (req.method=="POST" && req.param("id") == undefined) {
   	OptionCategory.create({
   		name:req.param("name"),
      // option: ["55c74a6f3b516d1b65af3571","55c39cde12ac3eb54d51a539"]
   		}).exec(function(err, model){
     		if (err)
     			return res.json({message: err.ValidationError});
     		else
     			return res.json({message: "GG"});
   	});
    }
      OptionCategory.find( function(err, doc) {
          return res.view({optioncategories:doc});
        });      
  },


  /**
   * `OptionCategoryController.read()`
   */
  read: function (req, res) {
    if (req.param("id")) {
        OptionCategory.find({id: req.param("id")} ,function(err, doc) {
          return res.send(doc);
      });
    } else {
        OptionCategory.find().populate("option").exec( function(err, doc) {
          return res.json({elements: doc});
      });
    }
  },

  read_lucas: function(req, res) {
     OptionCategory.find().populate("option").exec( function(err, doc) {
          var result = new Array();
          doc.forEach(function(entry){
          var option_ids = new Array();
            entry.option.forEach(function(e){
              option_ids.push(e.id);
            });
            result.push({"option_ids":option_ids , "name": entry.name, "createdAt": entry.createdAt, "updatedAt":entry.updatedAt, "id":entry.id})
          });
          return res.json(result);
      });
  },


  /**
   * `OptionCategoryController.delete()`
   */
  delete: function (req, res) {
        OptionCategory.destroy({id:req.param("id")}).exec(function(err, doc) {
          return res.redirect('/optioncategory/create');
      });
  },


  /**
   * `OptionCategoryController.update()`
   */
  update: function (req, res) {
    if (req.method=="POST") {
     
      OptionCategory.update({id:req.param("id")},{
        name:req.param("name"),
          }).
      exec(function(err,model) {
            if (err) {
          return res.json({
            message: err.ValidationError
              });
            }

      });
      res.redirect(307, '/optioncategory/create/');
    }
    OptionCategory.findOne({id: req.param("id")} ,function(err, doc) {
          return res.view({option:doc});
      }); 
  }
};

