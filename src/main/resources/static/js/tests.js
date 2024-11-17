
var projectId;
var charts;
var tests = new Set();
var projectCharts;
var projectTests;

$(function(){
    projectId = $("#projectId").data("id");
    getCharts();
    getTests();
})

function getCharts(){
    $.get(`/api/projects/${projectId}/charts`, function(items) {
        projectCharts = items;
    })
}

function getTests(){
    let tests = $("#tests");
    $.get(`/api/projects/${projectId}/tests`, function(items) {
           projectTests = items;
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
        "data-id": item.id
    });
    let testRelease = $("<h5>", { text: item.release });
    let testType = $("<span>", { text: item.testType });
    let testStartAt = $("<span>", { text: item.start });
    test.append(testRelease).append(testType).append(testStartAt);
    return test;
}

   $(document).on("click",".test",function(e) {
       e.preventDefault();
       let id = $(this).data("id");
       if(tests.has(id)){
            $(this).removeClass("active");
            tests.delete(id);
       } else {
            $(this).addClass("active");
            tests.add(id);
       }
   })

   function createCharts(ids){

   $.get('/api/data', { 'testId': id}, function(data){

           });


    let chartsDiv = $("myCharts");
    let test = projectTests.fond(({id}) => id === ids);
    let testGroup = $('<div>', { class: "test-group col", "data-id": ids}):
    let testName = $('<h6>', { class: "test-name", "data-id": ids}):
    let testType = $('<p>', { class: "", text: test.testType.name}):
    let testDate = $('<p>', { class: "", text: test.start + " - " + test.end}):

    testGroup.append(testName).append(testType).append(testDate);

   function createCharts(){
        $.each(m1, function(k1,v1){
            $.each(projectCharts, function(i, item){
                let value = v1[item.id];
                let chartBlock = $("<div>", {class: "canvas-wrapper"});
                let canvas = $("<canvas>", {id: "myChart" + k1 + item.id, class: "canvas"});
                charts.push(createChart(value, item, canvas));
                chartBlock.append(canvas);
            })
        })
    }

   }

    function createChart(value, item, ctx){
        let datasets = [];
        let labelsMaxSize=0
        let labelsMaxIndex=0
        let labels = 0;

        if(value != null){
            $.each(value, function(i, val){
                datasets.push({
                    borderColor: colors[i],
                    borderWidth: 1,
                    data: val.values,
                    label: val.metricName,
                    radius: 1
                });
            })
            labels = value[0].labels;
        }

        let chart = new Chart(ctx, {
            type: 'line',
            data: {
                labels:labels,
                datasets: datasets
            },
            options: {
                responsive: true,
                animation false,
                plugins: {
                    legend: {
                        position: 'right'
                    },
                    title: {
                        display: true,
                        text: item.title
                    },
                    decimation: {
                        algorithm: 'lttb',
                        enabled: true,
                        samples: 500
                    }
                },
                scales: {
                    x: {
                        display: true,
                        title: {
                            display: true,
                            text: item.scaleX
                        }
                    },
                    y: {
                        display:true,
                        title: {
                            display: true,
                            text: item.scaleY
                        }
                    }
                }
            }
        });
        return chart;

    }