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
