
class TotalTable {

      map = new Map();

       constructor(hashFunction) {
         this.hashFunction = hashFunction
       }

       getPanel(panelId){
            return this.map.get(panelId);
       }

       addData(panelId, metricName, testId, value){
            let panel = this.map.get(panelId);
            if(panel == null){
                panel = {};
                panel.name = metricName;
                panel.values = new Map();
                this.map.set(panelId, panel);
            }
            panel.values.set(testId, value);
       }

     add(key, testId, value){
        var row = this.map.get(key);
        if(row != null){
            row.set(testId, value);
        } else {
            var col = new Map();
            col.set(testId, value);
             this.map.set(key,col);
        }
     }

     getTable(){
        let table =$("<table>", { class:"table table-sm"});
        let thead =$("<thead>");
        let theadTr =$("<tr>");
        theadTr
        .append($("<th>"))
        .append($("<th>"));
        for (const testId of testSet) {
            let name = allTests.get(testId).release;
            theadTr.append($("<th>", { text : name}));
        }
        thead.append(theadTr);
        let tbody =$("<tbody>");
        let map = this.map;
         this.map.forEach((value, key) => {
          let tbodyTr = $("<tr>");
            tbodyTr
            .append($("<td>")
                .append($("<input>", { class: 'item-checkbox', type: 'checkbox', enable: false, name: "", "data-chart-id": key }))
            ).append($("<td>", { text: value.name }));
               for (const testId of testSet) {
                    let val = value.values.get(testId)
                    let td = $("<td>", { text: val});
                    tbodyTr.append(td);

               }
               tbody.append(tbodyTr);
         });

        table.append(thead).append(tbody);
     return table;
     }
}

 $(document).on("change",".item-checkbox",function(e) {
    let chartId = $(this).data("chart-id");
   if($(this).is(":checked")){
      $(`.canvas-wrapper[data-id='${chartId}']`).show();
   } else {
      $(`.canvas-wrapper[data-id='${chartId}']`).hide();
   }
  })
