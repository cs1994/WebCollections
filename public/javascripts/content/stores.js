
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.PersonalStore = Reflux.createStore({
        listenables: [global.actions.personalAction],
        init: function(){
            this.labelNum = 0;
        },
        getInitialState:function(){
            return{
                labelNum:this.labelNum,
            }
        },
        updateStore: function(){
            this.trigger({
                labelNum:this.labelNum,
            })
        },
        onChoseLabel:function(num){
        this.labelNum=num;
            this.updateStore();
        },
    });
}(window.React, window.ReactRouter, window.Reflux, window));