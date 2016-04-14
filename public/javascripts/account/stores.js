
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.registerStore = Reflux.createStore({
        listenables: [global.actions.registerAction],
        init: function(){
            this.notices = {}
        },
        getInitialState:function(){
            return{
                notices:this.notices
            }
        },
        updateStore: function(){
            this.trigger({
                notices:this.notices,
            })
        },
        onSendEmail:function(email){
            var url ="/register/sendEmail?email="+email;
            var successFunc = function (result) {
                console.log("发送成功");
            }.bind(this);
            ajaxGet(url,successFunc);
        },
        onSetPassWord:function(data){
            ajaxPost("/customer/register/email", data, function(res){
                if(res.errCode == 0){
                    console.log("注册成功")
                    window.location.href = "/login";
                }else{
                    alert(res.msg);
                }
            }.bind(this));
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));