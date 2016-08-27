$(function () {

    $("#a_logout").bind("click", function () {
        $.get("/adm/dologout", function () {
            window.location.href = "/adm/login";
        })
    });

});


var manyfile = false;
function fileupload(id) {
    $("#" + id).find("input[type='file']").fileupload({
        url: "/adm/upload",
        singleFileUploads: true,
        add: function (e, data) {
            $("#" + id).find(".showimg").attr("imgid", "");
            var uploadErrors = [];
            var filename = data.originalFiles[0]['name'];
            if (filename.substring(filename.length - 3, filename.length) != "jpg" && filename.substring(filename.length - 3, filename.length) != "png") {
                uploadErrors.push('只能上传jpg,png后缀的文件');
            }

            if (!manyfile) {
                if (data.originalFiles.length > 1) {
                    uploadErrors.push('只能上传1个文件');
                    manyfile = true;
                }
            }

            if (data.originalFiles[0]['size'] && data.originalFiles[0]['size'] > 1024 * 1024 * 10) {
                uploadErrors.push('上传文件不能大于10MB');
            }

            if (uploadErrors.length > 0) {
                layer.msg(uploadErrors.join("\n"));
            }
            else {
                $("#" + id).find('.progress').show();
                $("#" + id).attr("disabled", "disabled");
                data.submit();
            }
        },
        progressall: function (e, data) {

            var progress = parseInt(data.loaded / data.total * 100, 10);
            $("#" + id).find('.progress-bar').css(
                'width',
                progress + '%'
            ).text(progress + '%');
        },
        done: function (e, data) {
            data = data["result"];
            var imgid = data["imgid"];
            var imgname = data["imgname"];
            $("#" + id).find(".showimg").html("<a  target='_blank' href=\"/view/" + imgid + "/\">查看 " + imgname + "</a>");
            $("#" + id).find(".showimg").attr("imgid", imgid);
            $("#" + id).removeAttr("disabled");
            setTimeout("hideprocess(\"" + id + "\")", 3000);

//                var win = art.dialog.top;
//                win.artDialog.list['dia_ad_recommend'].size(360, 650).position("50%", "50%");

        }
    });
}


function hideprocess() {
    //上传完毕，5秒钟后消失。并且使得进度条
    $('#progress').hide();
    //进度值减为0
    $('.progress-bar').css(
        'width', '0%'
    );
}
