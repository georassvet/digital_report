
var projectId;
var charts;
var testSet = new Set();;
var chartSet = new Set();;


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
            let panelGroup = $("<div>", {class: "panel-group", 'data-id': item.id});
            dashboard.append(panelGroup);
        })
    })
}

function getTests(){
    let tests = $("#tests");
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
        class: "test",
        "data-id": item.id,
        "data-name": item.release
    });
    let testRelease = $("<h5>", { text: item.release });
    let testType = $("<span>", { text: item.testType });
    let testStartAt = $("<span>", { text: item.start });
    test.append(testRelease).append(testType).append(testStartAt);
    return test;
}

function deleteDataset(testId) {
    testSet.delete(testId);
    if (testSet.size > 0){
        $.each(chartSet, function(i, chart) {
    let data = chart.data;
    data.datasets.find((dataset, index) => {
        if (dataset.id === 'myId') {
           data.datasets.splice(index, 1);
           return true; // stop searching
        }
    });
    })
}
}




   $(document).on("click",".test",function(e) {
    e.preventDefault();
    var testId = $(this).data("id");
    if (testSet.has(testId)){
        $(this).removeClass("active");
       deleteDataset(testId);

    } else {
        $(this).addClass("active");
         testSet.add(testId);
         var testName = $(this).data("name");
             $.each(charts, function(i, chart) {
                     $.get('/api/data', { 'testId': testId, 'panelId': chart.id }, function(data){
                          createCharts(data, chart, testId, testName);
                     });
             })
        }
    })

    function createCharts(data, chart, testId, testName){
           let panelGroup = $(`.panel-group[data-id=${chart.id}]`);
           let dashboard = $("#dashboard");
            $.each(data, function(k1, v1) {
                $.each(v1, function(k2, v2) {
                let chartId = `${chart.id}_${v2.queryId}`;

                let chartItem = Chart.getChart(chartId);

                if(chartItem == null){
                     let ctx = $("<canvas>", { id: chartId });
                     chartItem = createChart(ctx, chart);
                     chartSet.add(chartItem);
                     panelGroup.append(ctx);
                }

                const newDataset = {
                      id: testId,
                      label: `${testName} ${v2.queryName}`,
                      data: v2.points
                };

                chartItem.data.datasets.push(newDataset);
                chartItem.update();
          })
       })
    }

    function createChart(ctx, item){
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
                tooltipFormat: 'MM',
                unit: 'minute',
                displayFormats: {
                    second: 'yyyy-mm-dd'
                }
             },
              title: {
                display: true,
                text: 'Date'
              }
            },
        y: {
          beginAtZero: true
        }
      }
    }
        });
   return chart;
 }
