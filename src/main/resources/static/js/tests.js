
var projectId;
var charts;

$(function(){
    projectId = $("#projectId").data("id");
    getCharts();
    getTests();
})

function getCharts(){
    $.get(`/api/projects/${projectId}/charts`, function(items) {
        charts = items;
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
    $(this).addClass("active");
    var id = $(this).data("id");
   $.get('/api/data', { 'testId': id}, function(data){

    });
 })

