var WebUtil = new function(){};
//邮箱验证
WebUtil.isEmail=function(str){
    //var emailPtr = /^[a-z0-9][a-z0-9._-]{2,}@neotel\.com\.cn$/i;
    var emailPtr = /^([a-zA-Z0-9]+[._-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[._-]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return emailPtr.test(str);
};

//强密码验证
WebUtil.isStrongPassword = function(str){
    var reg = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}$/;
    return reg.test(str);
};
WebUtil.GetQueryString=function(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null) return  unescape(r[2]); return null;
}

WebUtil.findfullemail=function(str){
    var emailurl = "";
    switch(str) {
        case "qq": emailurl="https://mail.qq.com";
            break;
        case "sina": emailurl="http://mail.sina.com.cn";
            break;
        case "163": emailurl="http://mail.163.com";
            break;
        case "126": emailurl="http://mail.126.com";
            break;
        case "gmail": emailurl="http://mail.google.com";
            break;
        case "188": emailurl="http://www.188.com";
            break;
        case "yahoo": emailurl="https://login.yahoo.com";
            break;
        case "hotmail": emailurl="http://www.hotmail.com";
            break;
        case "neotel": emailurl="http://exmail.qq.com/login";
            break;
        case "sohu": emailurl="http://mail.sohu.com";
            break;
        case "ebupt": emailurl="http://mail.ebupt.com";
            break;
        default:  emailurl="http://buscome.neoap.com/terra/login";
            break;
    }
    return emailurl;
}

WebUtil.timeFormat =function(timeNum){
    var format = function(time,format){
        var t = new Date(time);
        var tf = function(i){return (i < 10 ? '0' : '') + i};
        return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a){
            switch(a){
                case 'yyyy':
                    return tf(t.getFullYear());
                    break;
                case 'MM':
                    return tf(t.getMonth() + 1);
                    break;
                case 'mm':
                    return tf(t.getMinutes());
                    break;
                case 'dd':
                    return tf(t.getDate());
                    break;
                case 'HH':
                    return tf(t.getHours());
                    break;
                case 'ss':
                    return tf(t.getSeconds());
                    break;
            };
        });
    }

    return format(timeNum, 'yyyy-MM-dd HH:mm:ss');
};

WebUtil.isAllowedPic=function(type, size){
    var picTypes = { "image/jpeg": "", "image/jpg" : "", "image/png": "", "image/bmp": "", "image/gif": ""};
    return type.toLowerCase() in picTypes && size < 1024 * 1024; //pic size limited 1M
}
