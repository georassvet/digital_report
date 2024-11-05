
let charts;
let projectId;

$(function(){
    projectId = $("#projectId").data("id");
    getCharts();
})

function getCharts(){
    $.get(`/api/projects/${projectId}/charts`, function(items) {
        charts = items;
        createCharts();
    })
}

function createCharts(){
    let chartsDiv = $("#chart-items");
    chartsDiv.empty();
    for(let i=0; i< charts.length; i++){
        let chart = $("<div>", {
            class: "chart-item",
            "data-id": charts[i].id
        });

        let btnGroup1  = $("<div>", {class: "btn-group bg-light"});
        if(i!=0){
            let btnUp = $("<button>", {
                class: "btn btn-sm btn-prev border",
                text: "⯅",
                "data-order-id": i
            });
            btnGroup1.append(btnUp);
        }
        if(i !=charts.length - 1){
            let btnDown = $("<div>", {
                class: "btn btn-sm btn-next border",
                text: "⯆",
                "data-order-id": i
            });
            btnGroup1.append(btnDown);
        }

        let chartName = $("<a>", {
            class: "sub-label",
            href: `/projects/${projectId}/charts/${charts[i].id}`,
            text: charts[i].name
        });

        let btnGroup2 = $("<div>", {
            class: "btn-group bg-light"
        });


        let btnEdit = $("<a>", {
                    class: "btn btn-sm border",
                    href: `/projects/${projectId}/charts/${charts[i].id}/edit`,
                    text: "Edit"
        });
        let btnClone = $("<a>", {
                     class: "btn btn-sm border",
                      href: `/projects/${projectId}/charts/${charts[i].id}/clone`,
                     text: "Clone"
        });
         let btnDelete = $("<a>", {
                             class: "btn btn-sm border",
                             href: `/projects/${projectId}/charts/${charts[i].id}/delete`,
                             text: "Delete"
         });

         btnGroup2.append(btnEdit).append(btnClone).append(btnDelete);

        chart.append(btnGroup1).append(chartName).append(btnGroup2);
        chartsDiv.append(chart);
    }
}


   $(document).on("click",".btn-prev",function(e) {
    e.preventDefault();
    let id = $(this).data("order-id");
    if(id!=0){
        let prevId = id - 1;
        let temp = charts[id];
        charts[id] = charts[prevId];
        charts[prevId] = temp;
        createCharts();
    }

 })

$(document).on("click",".btn-next",function(e) {
    e.preventDefault();
    let id = $(this).data("order-id");
     if(id!= charts.length){
            let nextId = id + 1;
            let temp = charts[id];
            charts[id] = charts[nextId];
            charts[nextId] = temp;
            createCharts();
        }
 })

 $(document).on("click","#save-order",function(e) {
     e.preventDefault();
     let arr = [];
     for(let i=0; i<charts.length; i++){
        let obj = {};
        obj.chartId = charts[i].id;
        obj.chartOrder = i;
        arr.push(obj);
     }
     $.ajax({
        type: 'POST',
        url: `/api/update-chart-order`,
        data: JSON.stringify(arr),
        contentType: "application/json",
        success: function(data){

        }
     })
  })

