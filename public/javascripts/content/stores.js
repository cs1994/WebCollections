
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
        onAddSave:function(data,self){
            console.log("@@@@ " + JSON.stringify(data))
            var url="/customer/personal/web/add"
            ajaxJsonPost(url,data,function(){
                self.refs.addSave.close();
            })
        },
    });
}(window.React, window.ReactRouter, window.Reflux, window));