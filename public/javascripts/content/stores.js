
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.PersonalStore = Reflux.createStore({
        listenables: [global.actions.personalAction],
        init: function(){
            this.labelNum = 0;
            this.saveList = [];
        },
        getInitialState:function(){
            return{
                labelNum:this.labelNum,
                saveList:this.saveList,
            }
        },
        updateStore: function(){
            this.trigger({
                labelNum:this.labelNum,
                saveList:this.saveList,
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
        onGetPersonalSave: function () {
            var url="/customer/personal/web/list";
            var self= this;
            ajaxGet(url,function(json){
                self.saveList=json.result;
                console.log("@@" + JSON.stringify(self.saveList))
                self.updateStore();
            })
        },
        onDeletePersonalSave:function(id,index){
            var self =this;
            var url = "/customer/personal/web/delete?id="+id
            ajaxGet(url,function(json){
                self.saveList=json.result;
                self.saveList.splice(index,1);
                console.log("@@" + JSON.stringify(self.saveList))
                self.updateStore();
            })
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));