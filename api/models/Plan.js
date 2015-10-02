/**
 * Plan
 *
 * @module      :: Model
 * @description :: A short summary of how this model works and what it represents.
 * @docs		:: http://sailsjs.org/#!documentation/models
 */

module.exports = {

  attributes: {
  	
    name: {
      type: 'string',
      required: true
    },

    dimX: {
      type: 'int',
      required: true
    },

    dimY: {
      type: 'int',
      required: true
    },

    numShapes: {
      type: 'int',
      required:true
    },

    position: {
      type: 'string',
      required: true
    },

    tables: function(cb) {
      Table.find({plan_id: this.id}).done(function (err, tables) {
        cb(err, tables);
      })
    },
  },

  findLol:function(req, res) {
      var result = []
      var string_res;
      console.log("hyu");
      string_res = Plan.find({}).exec(function findCB(err, found) {
          while (found.length) {
              var tmp = found.pop().name;
              result.push(tmp);
          } 
          console.log("wtf + " + JSON.stringify(result));
          JSON.stringify(result);
      });
      console.log("double2 = " + string_res);
      return (string_res);
  }

};
