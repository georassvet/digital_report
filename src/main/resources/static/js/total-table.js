let tables = new Map();

class TotalTable {

constructor() {
    this.map = new Map();
  }
     add(key, value, testId){
        var row = this.map.get(key);
        if(row != null){
            row.set(testId, value);
        } else {
            var col = new Map();
            col.set(testId, value);
            this.map.set(key, col);
        }
     }
}