
var tasks = [];
var groups = [];

function groupByKey(array, key) {
   return array
     .reduce((hash, obj) => {
       if(obj[key] === undefined) return hash;
       return Object.assign(hash, { [obj[key]]:( hash[obj[key]] || [] ).concat(obj)})
     }, {})
}

$(function(){
    getTasks();
})

function getTasks(){
    $.get( "/tasks", function(tasks) {
        updateView(tasks);
    })
}

function updateView(tasks){
 var groups = $('#tasks');
  groups.empty();
     var grouped_tasks = groupByKey(tasks, 'groupName');
            for (const [key, grouped_task] of Object.entries(grouped_tasks)) {
               var group = $('<div>',{
                                    class:'group'
                                     }).append($('<h5>',{
                                                           class: 'group-header',
                                                           text: key
                                                }));

                $.each(grouped_task, function( j, item){

                    var task = $('<div>',{
                          class:'task'
                          }).attr('data-id',item.id);
                    var name = $('<a>',{
                                 class:'name',
                                 text: item.name,
                                 href:'/editSqlTask/' + item.id
                                });

                    var status = $('<div>',{
                                class: item.enable ? 'status started' : 'status stopped'
                              }).attr('data-id', item.id );
                              var btnGroup = $('<div>',{
                                    class:'btn-group'
                                });
                    var startBtn = $('<button>',{
                                     class:'btn btn-sm btn-secondary start'
                                 }).append(
                                    '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-play-fill" viewBox="0 0 16 16"><path d="m11.596 8.697-6.363 3.692c-.54.313-1.233-.066-1.233-.697V4.308c0-.63.692-1.01 1.233-.696l6.363 3.692a.802.802 0 0 1 0 1.393z"/></svg>'
                                 ).attr('data-id',item.id );
                    var stopBtn= $('<button>',{
                                    class:'btn btn-sm btn-secondary stop'
                                 }).append(
                                     '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-stop-fill" viewBox="0 0 16 16"><path d="M5 3.5h6A1.5 1.5 0 0 1 12.5 5v6a1.5 1.5 0 0 1-1.5 1.5H5A1.5 1.5 0 0 1 3.5 11V5A1.5 1.5 0 0 1 5 3.5z"/></svg>'
                                     ).attr('data-id',item.id );
                     var cloneBtn= $('<button>',{
                                           class:'btn btn-sm btn-secondary clone'
                                    }).append(
                                                     '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-plus-lg" viewBox="0 0 16 16"><path fill-rule="evenodd" d="M8 2a.5.5 0 0 1 .5.5v5h5a.5.5 0 0 1 0 1h-5v5a.5.5 0 0 1-1 0v-5h-5a.5.5 0 0 1 0-1h5v-5A.5.5 0 0 1 8 2Z"/></svg>'
                                                     ).attr('data-id',item.id );
                   var deleteBtn= $('<button>',{
                                                          class:'btn btn-sm btn-secondary delete'
                                                   }).append(
                                                                    '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x-lg" viewBox="0 0 16 16"><path d="M2.146 2.854a.5.5 0 1 1 .708-.708L8 7.293l5.146-5.147a.5.5 0 0 1 .708.708L8.707 8l5.147 5.146a.5.5 0 0 1-.708.708L8 8.707l-5.146 5.147a.5.5 0 0 1-.708-.708L7.293 8 2.146 2.854Z"/></svg>'
                                                                    ).attr('data-id',item.id );
                    btnGroup.append(startBtn).append(stopBtn).append(cloneBtn).append(deleteBtn);
                    task.append(name).append(status).append(btnGroup);
                    group.append(task);
                 })
                 groups.append(group);
            }
}


    $(document).on("click","#test",function(e) {
    e.preventDefault();
    var output = $('#output');
    output.empty();
    var form = new FormData($('#form')[0]);

           $.ajax({
                 type: 'post',
                 url: '/test',
                  data: form,
                  processData: false,
                  contentType: false,
                  cache: false,
                  success: function (data) {
                    if(data.errorMessage !== null && data.errorMessage!==""){
                        var textarea = $('<textarea>',{
                                               class:'form-control',
                                               type: 'text',
                                               text: data.errorMessage
                                         });
                        output.append(textarea);
                    }else{
                        var table = $('<table>',{
                                        class:'table table-sm table-light'
                                     });
                         var thead = $('<thead>');
                         var tr = $('<tr>');
                        $.each(data.columnNames, function( i, item){
                            tr.append($("<th>").text(item));
                        });
                        thead.append(tr);
                        table.append(thead);
                        var tbody = $('<tbody>');
                        $.each(data.rows, function( i, item){
                        var tr = $('<tr>');
                            $.each(item, function(j, elem){
                                tr.append($('<td>').text(elem));
                            });
                            tbody.append(tr);
                        });
                        table.append(tbody);

                     output.append(table);
                 }
           }
       })
  });

   $(document).on("click",".start",function(e) {
    e.preventDefault();
    var id = this.getAttribute("data-id");
   $.post('/start', { 'id': id},
        function(data){
  $('.status[data-id="'+id+'"]').removeClass("stopped").addClass("started");
    });
 })


    $(document).on("click",".stop",function(e) {
     e.preventDefault();
     var id = this.getAttribute("data-id");
    $.post('/stop', { 'id': id},
            function(data){
                   $('.status[data-id="'+id+'"]').removeClass("started").addClass("stopped");
        });
   })

   $(document).on("click",".clone",function(e) {
        e.preventDefault();
        var id = this.getAttribute("data-id");
       $.post('/clone', { 'id': id},
               function(tasks){
                    updateView(tasks);
           });
      })

 $(document).on("click",".delete",function(e) {
        e.preventDefault();
        var id = this.getAttribute("data-id");
        console.log(id);
       $.post('/delete', { 'id': id},
               function(tasks){
                    updateView(tasks);
           });
      })