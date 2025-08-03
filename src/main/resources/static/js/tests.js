
let projectId;
let charts;
let testSet = new Set();
let allCharts = [];

let borderColors = ['rgba(245, 39, 39, 0.9)','rgba(31, 227, 99, 0.9)', 'rgba(31, 89, 227, 0.9)', 'rgba(152, 31, 227, 0.9)'];
let backgroundColors = ['rgba(245, 39, 39, 0.7)','rgba(31, 227, 99, 0.7)','rgba(31, 89, 227, 0.7)', 'rgba(152, 31, 227, 0.7)'];


$(function(){
    projectId = $("#projectId").data("id");
    getCharts();
    getTests();
})

function getCharts(){
    let dashboard = $("#dashboard");
    $.get(`/api/projects/${projectId}/charts`, function(items) {
        charts = items;
        $.each(items, function(i, item) {
            let accordionItem = $("<div>", { class: "accordion-item chart-row"});
            let accordionHeader = $("<div>", { class: "accordion-header", id: `row-${item.id}`});
            let accordionBtn = $("<button>", {
                class: "accordion-button collapsed",
                type: 'button',
                'data-bs-toggle': 'collapse',
                'data-bs-target' : `#collapse-${item.id}`,
                'aria-expanded': 'false',
                'aria-controls': `collapse-${item.id}`,
                text: item.title
            });
            let accordionCollapse = $("<div>", {
                class: "accordion-collapse collapse",
                'id': `collapse-${item.id}`,
                'aria-labelledby': `row-${item.id}`,

            });
            let accordionBody = $("<div>", { class: "accordion-body panel-group",'data-id':item.id });
            accordionHeader.append(accordionBtn);
            accordionCollapse.append(accordionBody);
            accordionItem.append(accordionHeader).append(accordionCollapse);
            dashboard.append(accordionItem);
        })
    })
}

function getTests(){
    let tests = $("#items");
    $.get(`/api/projects/${projectId}/tests`, function(items) {
           tests.empty();

           $.each(items, function(i, item) {
            let test = createTest(item);
            tests.append(test);
           })
    })
}

function createTest(item){
    let test = $("<div>", {
        class: "item",
        "data-id": item.id,
        "data-name": item.release
    });
    let testRelease = $("<h5>", { text: item.release });
    let tagGroup = $("<span>", { class: "tag-group"})
        .append($("<span>", { text: item.testType }))
        .append($("<span>", { text: item.start }));
    test.append(tagGroup).append(testRelease);
    return test;
}

function deleteDataset(testId) {
    allCharts.forEach(chart => {
       let data = chart.data;
        data.datasets.find((dataset, index) => {
                       if (dataset.id === testId) {
                           data.datasets.splice(index, 1);
                           return true; // stop searching
                        }
                   });
        if(data.datasets.length == 0){
            chart.canvas.closest('.canvas-wrapper').remove();
        }
        chart.update();
    });

    $(`tr[data-id="${testId}"]`).remove();
}

   $(document).on("click",".item",function(e) {
    e.preventDefault();
    var testId = $(this).data("id");
    if (testSet.has(testId)){
        testSet.delete(testId);
        $(this).removeClass("active");
        deleteDataset(testId);
    } else {
        $(this).addClass("active");
         testSet.add(testId);
         var testName = $(this).data("name");
         var size = testSet.size-1;
             $.each(charts, function(i, chart) {
                     $.get('/api/data', { 'testId': testId, 'panelId': chart.id }, function(data){
                          createCharts(data, chart, testId, testName, size);
                     });
             })
        }
    })

    $(document).on("click",".split-btn",function(e) {
            e.preventDefault();
            var chartId = $(this).data("id");
            let chartItem = Chart.getChart(chartId);
            let canvasWrapper = chartItem.canvas;
            let splitBlock = $(`.split-block[data-id="${chartId}"]`);
            if (splitBlock.length > 0) {
                splitBlock.empty();
            } else {
                splitBlock = $("<div>", {class : "split-block"});
            }

            chartItem.data.datasets.forEach((dataset, index) => {
                  let subChartId = "sub_" + chartId + "_" + index;
                  let subCanvasWrapper = $("<div>", {class : "sub-canvas-wrapper", "data-id": subChartId});
                  let subCanvas = $("<canvas>", {"id": subChartId, "class": "sub-canvas"});
                  let subChartItem = createChart(subCanvas, "", "", 'bottom');
                  subCanvasWrapper.append(subCanvas);
                  splitBlock.append(subCanvasWrapper);
                  subChartItem.data.datasets.push(dataset);
                  subChartItem.update();
            })
            $(canvasWrapper).after(splitBlock);
        })

    function createCharts(data, chart, testId, testName, size){
           let panelGroup = $(`.panel-group[data-id=${chart.id}]`);

            $.each(data, function(k1, v1) {
                $.each(v1, function(k2, v2) {
                let chartId = `${chart.id}_${v2.queryId}_${k2}`;
                let chartItem = Chart.getChart(chartId);

                if(chartItem == null){
                    let canvasWrapper = $("<div>", { class: "canvas-wrapper",  "data-id": chartId });
                    let canvasHeader = $("<div>", { class: "d-flex justify-content-between"});
                    let canvasName = $("<div>", { class: "canvas-name", text:`${v2.queryName}`});
                    let splitBtn = $("<button>", { class: "btn btn-sm btn-light border split-btn", text:"Split", "data-id": chartId});
                    canvasHeader.append(canvasName).append(splitBtn);
                    let chartTable = createTable(chartId);
                    let canvas = $("<canvas>", { id: chartId });
                    chartItem = createChart(canvas, chart.scaleX, chart.scaleY, 'right');
                    allCharts.push(chartItem);
                    canvasWrapper.append(canvasHeader).append(canvas).append(chartTable);
                    panelGroup.append(canvasWrapper);

                }

                let data =  v2.points.map(({x,y}) => ({"x": x - v2.testStart, "y":y}));

                const newDataset = {
                      borderWidth: 0.5,
                      radius:1,
                      pointRadius:0.5,
                      id: testId,
                      label: `${testName}`,
                      data: data, //v2.points,
                      //fill: true,
                      backgroundColor: backgroundColors[size],
                      borderColor: borderColors[size]
                };

                chartItem.data.datasets.push(newDataset);
                chartItem.update();

                let tr = $("<tr>", {"data-id": testId })
                                  .append($("<td>",{ text: testName }))
                                  .append($("<td>", { text: v2.min }))
                                  .append($("<td>", { text: v2.avg }))
                                  .append($("<td>", { text: v2.p90 }))
                                  .append($("<td>",{ text: v2.max }))

                $(`tbody[data-id=${chartId}]`).append(tr);
          })
       })
    }

    function createTable(chartId){
        let table = $("<table>", {class: "table table-sm table-light", id: "table_" + chartId })
        .append($("<thead>").append($("<tr>")
        .append($("<td>", {text: "name" }))
        .append($("<td>", {text: "min" }))
        .append($("<td>", {text: "avg" }))
        .append($("<td>", {text: "p90" }))
        .append($("<td>", {text: "max" }))
        )).append($("<tbody>", {'data-id' : chartId }));
        return table;
    }

    function createChart(ctx, scaleX ,scaleY, position){
    var datasets = [];
    var chart =  new Chart(ctx, {
     type: 'line',
     data: {
      datasets: datasets
    },
    options: {
      scales: {
       x: {
              type: 'time',
              time: {
                // Luxon format string
//                 tooltipFormat: 'mm',
//                 stepSize: 5,
//                 unit: 'second',
//                 displayFormats: {
//                        minute : "mm"
//                 }
                minUnit: 'millisecond',
                unit: 'minute',
 //                displayFormats: {
//                                  minute: 'HH:mm:ss'
//                },
//                tooltipFormat: 'HH:mm:ss',

                        // round: 'day'
                        tooltipFormat: 'mm',
                        displayFormats: {
                            millisecond: 'HH:mm:ss.SSS',
                            second: 'HH:mm:ss',
                            minute: 'mm',
                            hour: 'HH'
                        }
             },
              title: {
                display: true,
                text: scaleX
              }
            },
        y: {
          type: 'linear',
          title: {
                  display: true,
                  text: scaleY
                 }
        }
      },
      animation: false,
      interaction: {
        intersect: false,
        mode: 'index'
      },
      tooltips: {
                      mode: 'index',
                      //enabled: false,
                      intersect: false,
                  },
      plugins: {
        legend: {
            position: position,
            font: {
                size: 6
            },
            fullSize: true
            }
        },
        decimation: {
            algorithm: 'lttb',
            enabled: true
        }
      }

    });

   return chart;
 }
