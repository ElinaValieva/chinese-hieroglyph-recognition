$(function () {
    var droppedFiles = false;
    var fileName = '';
    var $dropzone = $('.dropzone');
    var uploading = false;
    var $syncing = $('.syncing');
    var $done = $('.done');
    var $bar = $('.bar');

    $dropzone.on('drag dragstart dragend dragover dragenter dragleave drop', function (e) {
        e.preventDefault();
        e.stopPropagation();
    })
        .on('dragover dragenter', function () {
            $dropzone.addClass('is-dragover');
        })
        .on('dragleave dragend drop', function () {
            $dropzone.removeClass('is-dragover');
        })
        .on('drop', function (e) {
            droppedFiles = e.originalEvent.dataTransfer.files;
            fileName = droppedFiles[0]['name'];
            $('.filename').html(fileName);
            $('.dropzone .upload').hide();
        });

    $("#uploadFile").bind('click', function () {
        startUpload();
    });

    $("input:file").change(function () {
        fileName = $(this)[0].files[0].name;
        $('.filename').html(fileName);
        $('.dropzone .upload').hide();
    });

    function startUpload() {
        if (!uploading && fileName != '') {
            uploading = true;
            $("#uploadFile").html('Uploading...');
            $dropzone.fadeOut();
            $syncing.addClass('active');
            $done.addClass('active');
            $bar.addClass('active');
            uploadFile();
        }
    }

    function uploadFile() {
        event.preventDefault();
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/upload",
            data: new FormData($('#fileUploadForm')[0]),
            processData: false, //prevent jQuery from automatically transforming the data into a query string
            contentType: false,
            cache: false,
            timeout: 600000
        }).done(function (data) {
            showDone(data);
        }).fail(function (e) {
            console.log("ERROR : ", e);
        });
    }

    function showDone(data) {
        $("#uploadFile").html('Done');
        console.log("SUCCESS : ", data);
        var results = data;
        alert(JSON.stringify(results));
        $("#contentResults").empty();
        for (var i = 0; i < results.length; i++) {
            $("#contentResults").append("<img src='images/" + results[i].codeResult + ".png'><br/>" +
                "<label class='text-warning'>" + results[i].result + "</label><br/><br/>"
            )
        }
    }
});