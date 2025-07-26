
var projectId;
var charts;
var testSet = new Set();
var chartSet = new Set();

let borderColors = ['rgba(245, 39, 39, 0.9)','rgba(31, 227, 99, 0.9)', 'rgba(31, 89, 227, 0.9)', 'rgba(152, 31, 227, 0.9)'];
let backgroundColors = ['rgba(245, 39, 39, 0.9)','rgba(31, 227, 99, 0.9)','rgba(31, 89, 227, 0.9)', 'rgba(152, 31, 227, 0.9)'];


$(function(){
    getCheck();
})

function getCheck(){
    $.get(`/api/data/check`, function(item) {
          let ctx = $("<canvas>" , {id:1});
          let chartItem = createChart(ctx);
          $("#dash").append(ctx);

                let avgMapData =  item.avgMap.map(({x,y}) => ({"x": x , "y":y}));

                            const avgMapDataset = {
                                  borderWidth: 1,
                                  radius:1,
                                  pointRadius:0.3,
                                  tension: 0.3,
                                  label: `avgMap`,
                                  data: avgMapData, //v2.points,
                                  backgroundColor: backgroundColors[0],
                                  borderColor: borderColors[0]
                            };

                            chartItem.data.datasets.push(avgMapDataset);

                             let stdPlus2 =  item.stdPlus2.map(({x,y}) => ({"x": x , "y":y}));

                                                        const stdPlus2Dataset = {
                                                              borderWidth: 1,
                                                              radius:1,
                                                              pointRadius:0.3,
                                                              tension: 0.3,
                                                              label: `stdPlus2`,
                                                              data: stdPlus2, //v2.points,
                                                              backgroundColor: backgroundColors[1],
                                                              borderColor: borderColors[1]
                                                        };

                                                        chartItem.data.datasets.push(stdPlus2Dataset);
                               let stdMinus2 =  item.stdMinus2.map(({x,y}) => ({"x": x , "y":y}));

                                                                                     const stdMinus2Dataset = {
                                                                                           borderWidth: 1,
                                                                                           radius:1,
                                                                                           pointRadius:0.3,
                                                                                           tension: 0.3,
                                                                                           label: `stdMinus2`,
                                                                                           data: stdMinus2, //v2.points,
                                                                                           backgroundColor: backgroundColors[1],
                                                                                           borderColor: borderColors[1]
                                                                                     };

                                                                                     chartItem.data.datasets.push(stdMinus2Dataset);
                             let test =  item.test.map(({x,y}) => ({"x": x , "y":y}));

                                                                                                                  const testDataset = {
                                                                                                                        borderWidth: 1,
                                                                                                                        radius:1,
                                                                                                                        pointRadius:0.3,
                                                                                                                        tension: 0.3,
                                                                                                                        label: `test`,
                                                                                                                        data: test, //v2.points,
                                                                                                                        backgroundColor: backgroundColors[2],
                                                                                                                        borderColor: borderColors[2]
                                                                                                                  };

                                                                                                                  chartItem.data.datasets.push(testDataset);


                            chartItem.update();

    })
}



    function createChart(ctx){
    var datasets = [];
    var chart =  new Chart(ctx, {
     type: 'line',
     data: {
      datasets: datasets,
      borderWidth:1
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
//                 minUnit: 'millisecond',
               unit: 'minute',
//                 displayFormats: {
//                                  minute: 'HH:mm:ss'
//                },
//                tooltipFormat: 'HH:mm:ss',

                        // round: 'day'
                        tooltipFormat: 'yyyy-MM-dd HH:mm',
                        displayFormats: {
                            millisecond: 'HH:mm:ss.SSS',
                            second: 'HH:mm:ss',
                            minute: 'HH:mm',
                            hour: 'HH',
                            day: 'dd HH'
                        }
             },
              title: {
                display: true,
                text: "ms"
              }
            },
        y: {
          type: 'linear',
          title: {
                  display: true,
                  text: "ms"
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
            position: 'bottom',
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
