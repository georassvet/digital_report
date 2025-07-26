class Config  {
    map = new Map();

    addRow(key, val){
        if(this.map.has(key)){
             let values =  this.map.get(key);
             val.forEach(values.add, values);
             this.map.set(key, values);
        } else {
            this.map.set(key, val);
        }
    }

    deleteValue(key, val){
            let values =  this.map.get(key);
            values.delete(val);

    }

    stringify(){
        let obj = {};
        obj.configs = new Array();
        this.map.forEach(function(value, key) {
            let row = {};
                row.host = key.host;
                row.namespace = key.namespace;
                row.containers = [...value];
            obj.configs.push(row);
        })
        return JSON.stringify(obj);

    }

};

const config = new Config();

$(function(){
    getConfig();
})

function getConfig(){
    let configDiv = $("#config");
    $.get(`/api/config`, function(data) {
         $.each(data.configs, function( i, item){
            var key = JSON.stringify({host: item.host, namespace: item.namespace});
            var value = new Set(item.containers);
            config.addRow(key, value);
            let row = $("<div>", {class : ""});
                let host = $("<div>", {class : "host", text: item.host });
                let namespace = $("<div>", {class : "namespace", text: item.namespace});
                let containersDiv = $("<div>", {class : "containers"});
                $.each(item.containers, function( j, container){
                   let containerBlock = $("<div>", {class : "container"});
                   let containerDiv = $("<div>", {class : "name", text: container});
                   let containerBtn = $("<button>", {class: "btn btn-sm btn-light border delete", text: "Delete" , 'data-key': key, 'data-value': container });
                   containerBlock.append(containerDiv).append(containerBtn);
                   containersDiv.append(containerBlock);
                })
            row.append(host).append(namespace).append(containersDiv);
            configDiv.append(row);
         })

    })
}

$(document).on("click",".delete",function(e) {
     e.preventDefault();
     let key = JSON.stringify($(this).data("key"));
     let value = $(this).data("value");
     config.deleteValue(key,value);
     $(this).remove();

})

$(document).on("click","#setConfig",function(e) {
     e.preventDefault();

    let conf = config.stringify();
    console.log(conf);

     $.ajax({
        type: 'POST',
        url: `/api/config/set`,
        data: conf,
        contentType: "application/json",
            success: function(data){

            }
     })
  })
