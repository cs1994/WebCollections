
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
    });
}(window.React, window.ReactRouter, window.Reflux, window));