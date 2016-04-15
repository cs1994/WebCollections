
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.contentStore = Reflux.createStore({
        listenables: [global.actions.contentAction],
        init: function(){
            this.state = 0;
            this.email="";
            this.findState = 0;
        },
        getInitialState:function(){
            return{
                state:this.state,
                email:this.email,
                findState:this.findState
            }
        },
        updateStore: function(){
            this.trigger({
                state:this.state,
                email:this.email,
                findState:this.findState
            })
        },
        onSetFindState:function(tag){
            this.findState=tag;
            this.updateStore();
        },
        onSendEmail:function(email){
            var url ="/register/sendEmail?email="+email;
            var successFunc = function (result) {
                console.log("发送成功");
                this.state=1;
                this.email=email;
                //console.log("@@@@@@@@@@@@@@@@ " +this.state)
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
        onResetPassword:function(email){
            var checkEmailUrl = "/check/email?email="+ email;
            ajaxGet(checkEmailUrl,function(res){
                if(res.is_exists){
                    var confirmUrl="/mail/confirm?email="+email;
                    var successFunc = function (result) {
                        console.log("发送成功");
                        this.findState=2;
                        this.email=email;
                        this.updateStore();
                        //console.log("!!!!!!!!!!!!!!!@@@ "+this.findState)
                    }.bind(this);
                    ajaxGet(confirmUrl,successFunc);
                }
                else
                toastr.warning("账户不存在")
            }.bind(this))
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));