

function ajaxPost(url,postData,successFunc){
    $.ajax({
        url:url,
        dataType:'json',
        type:'POST',
        data:postData,
        success:function(data){
            var errCode=data.errCode;
            var msg=data.msg;
            if(errCode == 0){
                //alert("success");
                successFunc(data)
            }else{
                //alert("failed");
                //toastr.error(errmsg);
                console.log("errcode = "+errCode+" errmsg = "+msg);
            }
        }.bind(this),
        error:function(xhr,status,err){
            // alert("error");
            console.log(xhr,status,err.toString());
            //toastr.error(err);
        }.bind(this)
    })
}

function ajaxJsonPost(url, postData, successFunc) {
    var sendData = JSON.stringify(postData);
    //Debugger.log("ajaxJsonPost: sendData=" + sendData);
    $.ajax({
        url: url,
        dataType: 'json',
        contentType: 'application/json',
        type: 'POST',
        data: sendData,
        success: function (data) {
            var errcode = data.errCode;
            var errmsg = data.msg;
            if (errcode != 0) {
                Debugger.log('errcode=' + errcode + ', errmsg=' + errmsg);
                //toastr.error(errmsg);
            } else {
                successFunc(data);
            }
        }.bind(this),
        error: function (xhr, status, err) {
            console.error(url, xhr, status, err.toString());
            //toastr.error(err);
        }.bind(this)
    });
}

function ajaxGet(url,successFunc){
    $.ajax({
        url:url,
        dataType:'json',
        type:'GET',
        async: false,
        success:function(res){
            console.log("@@@@@@@@@ "+ JSON.stringify(res))
            if(res.errCode == 0) successFunc(res);
            else {
                //toastr.error(res.errmsg);
                //Debugger.log("errcode: " + res.errcode + ", errmsg: " + res.errmsg);
            }

        }.bind(this),
        error:function(xhr,status,err){
            console.log(xhr,status,err.toString());
        }.bind(this)
    })
}


function ajaxDataGet(url,data,successFunc){
    $.ajax({
        url:url,
        dataType:'json',
        type:'GET',
        data:data,
        success:function(res){
            if(res.errCode == 0) successFunc(res);
            else {
                //toastr.error(res.errmsg);
                //Debugger.log("errcode: " + res.errcode + ", errmsg: " + res.errmsg);
            }

        }.bind(this),
        error:function(xhr,status,err){
            console.log(xhr,status,err.toString());
        }.bind(this)
    })
}