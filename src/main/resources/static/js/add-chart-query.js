 $(document).on("click","#testBtn",function(e) {
    e.preventDefault();
    var output = $('#output');
    output.empty();
    var form = new FormData($('#query-form')[0]);

           $.ajax({
                 type: 'post',
                 url: '/api/query/test',
                  data: form,
                  processData: false,
                  contentType: false,
                  cache: false,
                  success: function (data) {

                        var textarea = $('<textarea>',{
                                               class:'form-control',
                                               type: 'text',
                                               text: data
                                         });
                        output.append(textarea);


           }
       })
  });
