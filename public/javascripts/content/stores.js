
(function(React, Router, Reflux, global) {
    global.stores = {};

    global.stores.AllStore = Reflux.createStore({
        listenables: [global.actions.allAction],
        init: function(){
            this.webList = [];

        },
        getInitialState:function(){
            return{
                webList:this.webList,

            }
        },
        updateStore: function(){
            this.trigger({
                webList:this.webList,

            })
        },
        onGetAllSave: function () {
            var url="/customer/web/list";
            var self= this;
            ajaxGet(url,function(json){
                self.webList=json.result;
                self.updateStore();
            })
        },
        onDeleteSave:function(id,index){
            var self =this;
            var url = "/customer/personal/web/delete?id="+id
            ajaxGet(url,function(json){
                //self.saveList=json.result;
                self.webList.splice(index,1);
                //console.log("@@" + JSON.stringify(self.saveList))
                self.updateStore();
            })
        },
        onSubmitComment:function(data,index){
            var url ="/customer/comment/add";
            var self=this;
            ajaxJsonPost(url,data,function(json){
                toastr.success("评论成功");
                var item = {"userInfo":{"id":$CONF$.id,"headImg":$CONF$.headImg,"nickName":$CONF$.nickName},"content":data.content,
                    "id":json.id,"replyComment":{}}
                self.webList[index].commentList.push(item);
                self.updateStore();
                $("#input"+index).val("");
            })
        },
        onReplyComment:function(data,index,i){
            var url="/customer/comment/reply";
            var self =this;
            ajaxJsonPost(url,data,function(json){
                //var commentList = self.saveList[index].commentList;
                //var comment = commentList[i];
                var item ={id:json.id,toId:data.toId,content:data.content,saveId:data.saveId,replyUserInfo:{
                    id:$CONF$.id,headImg:$CONF$.headImg,nickName:$CONF$.nickName
                }};
                self.webList[index].commentList[i].replyComment=item
                console.log("!!!!!!!!!! " +JSON.stringify(self.webList[index].commentList[i]))
                self.updateStore();
                toastr.success("回复成功");
            })
        },
    });

    global.stores.PersonalStore = Reflux.createStore({
        listenables: [global.actions.personalAction],
        init: function(){
            this.labelNum = 0;
            this.passFlag = 0;
            this.saveList = [];
            this.userInfo={};
        },
        getInitialState:function(){
            return{
                labelNum:this.labelNum,
                passFlag:this.passFlag,
                saveList:this.saveList,
                userInfo:this.userInfo,
            }
        },
        updateStore: function(){
            this.trigger({
                labelNum:this.labelNum,
                passFlag:this.passFlag,
                saveList:this.saveList,
                userInfo:this.userInfo,
            })
        },
        onGetuserInfo:function(){
            var self=this;
            var url="/customer/userInfo/get";
            ajaxGet(url,function(json){
                self.userInfo=json.info;
                self.updateStore();
            })
        },
        onCheckOldPass:function(data){
            var url="/customer/password/check";
            var self = this;
            ajaxJsonPostTwo(url,data,function(json){
                self.passFlag =1;self.updateStore();
            },function(json){self.passFlag =2;self.updateStore();})

        },
        onChangePass:function(data,me){
            var url="/customer/password/change";
            var self = this;
            ajaxJsonPostTwo(url,data,function(json){
                toastr.success("修改成功！")
                me.refs.changePassword.close();
            },function(json){self.passFlag =2;})
            self.updateStore();
        },
        onChoseLabel:function(num){
        this.labelNum=num;
            this.updateStore();
        },
        onAddSave:function(data,me){
            //console.log("@@@@ " + JSON.stringify(data))
            var url="/customer/personal/web/add"
            var self =this;
            ajaxJsonPost(url,data,function(json){
                me.refs.addSave.close();
                var item={id:json.id,url:data.url,des:data.description,secret:data.secret,number:0,
                    content:json.content,insertTime:json.time,commentNum:0, flag:1,label:data.label,commentList:[]}
                var list = self.saveList
                list.unshift(item)
                self.saveList = list;
                self.updateStore();
                toastr.success("收藏成功")
            })
        },
        onGetPersonalSave: function () {
            var url="/customer/personal/web/list";
            var self= this;
            ajaxGet(url,function(json){
                self.saveList=json.result;
                self.updateStore();
            })
        },
        onDeletePersonalSave:function(id,index){
            var self =this;
            var url = "/customer/personal/web/delete?id="+id
            ajaxGet(url,function(json){
                //self.saveList=json.result;
                self.saveList.splice(index,1);
                //console.log("@@" + JSON.stringify(self.saveList))
                self.updateStore();
            })
        },
        onChangeUserInfo:function(data,me){
            var url ="/customer/userInfo/change";
            var self =this;
            var oldData=self.userInfo;
            ajaxJsonPostTwo(url,data,function(json){
                self.userInfo={id:oldData.id,email:oldData.email,headImg:oldData.headImg, sex:data.gen, phone:data.phone,
                    birthday:data.birthday,nickName:data.nickName,insertTime:oldData.insertTime,commentNum:oldData.commentNum};
                self.updateStore();
               toastr.success("修改成功")
            },function(json){toastr.warning("修改失败")});
            me.refs.changeInfo.close();
        },
        onReplyComment:function(data,index,i){
            var url="/customer/comment/reply";
            var self =this;
            ajaxJsonPost(url,data,function(json){
                //var commentList = self.saveList[index].commentList;
                //var comment = commentList[i];
                var item ={id:json.id,toId:data.toId,content:data.content,saveId:data.saveId};
                self.saveList[index].commentList[i].replyComment=item
                self.updateStore();
                toastr.success("回复成功");
            })
        },
    });
    global.stores.TaskStore = Reflux.createStore({
        listenables: [global.actions.taskAction],
        init: function(){
            this.taskList = [];
            this.unStartList = [];
            this.unFinishList = [];
        },
        getInitialState:function(){
            return{
                taskList:this.taskList,
                unStartList:this.unStartList,
                unFinishList:this.unFinishList,
            }
        },
        updateStore: function(){
            this.trigger({
                taskList:this.taskList,
                unStartList:this.unStartList,
                unFinishList:this.unFinishList,
            })
        },
        onAddTask:function(data){
            var url="/customer/task/add";
            var self=this;
            ajaxJsonPostTwo(url,data,function(json){
                var item = {id:json.id,content:data.content,state:data.state,insertTime:json.insertTime};
                self.taskList.unshift(item)
                self.updateStore();
                //console.log("!!!!!!!!!!!!!!! " + JSON.stringify(self.taskList))
                toastr.success("添加成功")
             },function(json){toastr.warning("添加失败")})
        },
        onGetAllTask:function(){
            var url="/customer/task/list";
            var self=this;
            ajaxGet(url,function(json){
                self.taskList=json.list;
                self.updateStore();
            })
        },
        onChangeTaskState:function(id,state,index){
            var url = "/customer/task/state/change?id="+id+"&state="+state;
            var self = this;
            ajaxGet(url,function(json){
                var item = self.taskList[index];
                item.state = state;
                var oldList = self.taskList;
                var list = oldList.slice(0,index)
                    list.push(item)
                self.taskList=list.concat(oldList.slice(index+1));
                self.updateStore();
                $("#edit"+index).toggle("slow");
                toastr.success("修改成功")
            })
        },
        onDeleteTask:function(id,index){
            var url = "/customer/task/delete?id="+id;
            var self = this;
            ajaxGet(url,function(){
                var oldList = self.taskList;
                self.taskList=oldList.slice(0,index).concat(oldList.slice(index+1));
                self.updateStore();
                $("#edit"+index).toggle("slow");
                toastr.success("删除成功")
            })
        },
        onGetListByState:function(state){
            var url = "/customer/task/getByState?state="+state;
            var self = this;
            ajaxGet(url,function(json){
                self.taskList=json.list;
                self.updateStore();
            })
        },
        onGetUnstart:function(){
            var url = "/customer/task/unFinshed";
            var urlTwo = "/customer/task/unStart";
            var self = this;
            ajaxGet(url,function(json){
                self.unFinishList=json.list;
            })
            ajaxGet(urlTwo,function(json){
                self.unStartList=json.list;
                //console.log("@@@@@ " + JSON.stringify(self.unStartList))
            })
            self.updateStore();
        },

    });

    global.stores.CommentStore = Reflux.createStore({
        listenables: [global.actions.commentAction],
        init: function(){
            this.commentList = [];
            this.replyList = [];
            this.replyInfo={};
        },
        getInitialState:function(){
            return{
                commentList:this.commentList,
                replyList:this.replyList,
                replyInfo:this.replyInfo,

            }
        },
        updateStore: function(){
            this.trigger({
                commentList:this.commentList,
                replyList:this.replyList,
                replyInfo:this.replyInfo,

            })
        },
        onGetAllComments: function () {
            var url="/customer/comment/list";
            var self= this;
            ajaxGet(url,function(json){
                self.commentList=json.result;
                self.updateStore();
            })
        },
        onGetAllReply:function(){
            var url="/customer/comment/replay/list"
            var self= this;
            ajaxGet(url,function(json){
                self.replyList=json.result;

                self.updateStore();
            })
        },
        onDeleteComment:function(id,index){
            var url ="/customer/comment/delete?id="+id;
            var self=this;
            ajaxGet(url, function () {
                self.commentList.splice(index,1);
                self.updateStore();
                toastr.success("删除成功")
            })
        },
        onSetReplayInfo:function(data){
            this.replyInfo=data;
            this.updateStore();
        },
        onReplyComment:function(data,self){
            var url="/customer/comment/reply";
            ajaxJsonPost(url,data,function(json){
                toastr.success("回复成功");
                self.refs.replyComment.close();
                $("#commentReply").val("");
            })
        },

    });

    global.stores.SearchStore = Reflux.createStore({
        listenables: [global.actions.commentAction],
        init: function(){
            this.commentList = [];
        },
        getInitialState:function(){
            return{
                commentList:this.commentList,
            }
        },
        updateStore: function(){
            this.trigger({
                commentList:this.commentList,
            })
        },

    });

}(window.React, window.ReactRouter, window.Reflux, window));