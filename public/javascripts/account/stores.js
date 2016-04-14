
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.registerStore = Reflux.createStore({
        listenables: [global.actions.registerAction],
        init: function(){
            this.state = 0;
            this.email="";
        },
        getInitialState:function(){
            return{
                state:this.state,
                email:this.email
            }
        },
        updateStore: function(){
            this.trigger({
                state:this.state,
                email:this.email
            })
        },
        onSendEmail:function(email){
            var url ="/register/sendEmail?email="+email;
            var successFunc = function (result) {
                console.log("发送成功");
                this.state=1;
                this.email=email;
                this.updateStore();
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
        },
        onResetPassword:function(){
            var url ="/register/sendEmail?email="+email;
            var successFunc = function (result) {
                console.log("发送成功");
                this.state=1;
                this.email=email;
                this.updateStore();
            }.bind(this);
            ajaxGet(url,successFunc);
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));