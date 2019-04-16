$(function () {
        let current_fs, next_fs, previous_fs;
        let left, opacity, scale;
        let animating;
        let fileName = '';
        let uploading = false;

        $(".next").click(function () {
            if (animating) return false;
            animating = true;

            current_fs = $(this).parent();
            next_fs = $(this).parent().next();

            $("#progressbar li").eq($("fieldset").index(next_fs)).addClass("active");

            next_fs.show();
            current_fs.animate({opacity: 0}, {
                step: function (now, mx) {
                    scale = 1 - (1 - now) * 0.2;
                    left = (now * 50) + "%";
                    opacity = 1 - now;
                    current_fs.css({
                        'transform': 'scale(' + scale + ')',
                        'position': 'absolute'
                    });
                    next_fs.css({'left': left, 'opacity': opacity});
                },
                duration: 800,
                complete: function () {
                    current_fs.hide();
                    animating = false;
                },
                easing: 'easeInOutBack'
            });
        });

        $(".previous").click(function () {
            if (animating) return false;
            animating = true;

            current_fs = $(this).parent();
            previous_fs = $(this).parent().prev();

            $("#progressbar li").eq($("fieldset").index(current_fs)).removeClass("active");

            previous_fs.show();
            current_fs.animate({opacity: 0}, {
                step: function (now, mx) {
                    scale = 0.8 + (1 - now) * 0.2;
                    left = ((1 - now) * 50) + "%";
                    opacity = 1 - now;
                    current_fs.css({'left': left});
                    previous_fs.css({'transform': 'scale(' + scale + ')', 'opacity': opacity});
                },
                duration: 800,
                complete: function () {
                    current_fs.hide();
                    animating = false;
                },
                easing: 'easeInOutBack'
            });
        });

        $(".submit").click(function () {
            return false;
        });

        $('.upload-btn').bind('click', function () {
            startUpload();
        });

        $("input:file").change(function () {
            fileName = $(this)[0].files[0].name;
            $('.filename').html(fileName);
        });

        function startUpload() {
            if (!uploading && fileName != '') {
                uploading = true;
                uploadFile();
            }
        }

        function uploadFile() {
            event.preventDefault();
            var formData = new FormData();
            formData.append('file', $("input[name=file]")[0].files[0]);
            $.ajax({
                type: "POST",
                enctype: 'multipart/form-data',
                url: "/upload",
                data: formData,
                processData: false,
                contentType: false,
                cache: false,
                timeout: 600000
            }).done(function (data) {
                console.log(JSON.stringify(data));
            }).fail(function (e) {
                console.log("ERROR : ", e);
            });
        }
    }
);