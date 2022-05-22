 $(document).ready(function(){
        $('#uploadImage').click(function(){
          var form = $('#uploadform');
          form.submit();
          form.submit(function(){
            $.ajax({
              type: 'POST',
              url: '/bin/sling/uploadimage',
              data: form.serialize(),
              enctype: 'multipart/form-data',
              success: function(result) {
                alert("Success");
              }
            });
          });
        });
      });