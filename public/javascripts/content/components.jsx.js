
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    var stores = global.stores;
    var actions = global.actions;
    components.Header = React.createClass({
        mixins:[
            Reflux.connect(stores.PersonalStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        componentWillMount:function(){
            global.actions.personalAction.getuserInfo()
        },
        openModal(){
            this.refs.changePassword.open();
        },
        openInfoModal(){
            this.refs.changeInfo.open();
        },
        judgeOldPwd(){
            var oldpwd=$("#oldPassword").val();
            var data={oldpwd:oldpwd}
            global.actions.personalAction.checkOldPass(data)
        },
        onConfirm(){
            const self =this;
            if(self.state.data.passFlag ==1){
                var pwd=$("#passwordInput").val();
                var passw=$("#confirmInput").val();
                if(pwd.length<8 || pwd.length>20 || pwd!=passw){
                    toastr.warning("请根据提示填写")
                    return
                }
                var data={newpwd:pwd}
                global.actions.personalAction.changePass(data,self)
            }
            else toastr.warning("请输入正确的原始密码！")

        },
        judgeStrong(){
            var passWord=$("#passwordInput").val()
            if(7<passWord.length && passWord.length<21) $("#passP").css("display","none");
            else $("#passP").css("display","block");
        },
        judgeSame(){
            var passWord1=$("#passwordInput").val()
            var passWord2=$("#confirmInput").val()
            if(passWord1 == passWord2) $("#confirmP").css("display","none");
            else $("#confirmP").css("display","block");
        },
        infoToggle:function(){
            $("#collapseInfo").toggle();
        },
        handleUserPicChange:function(e){
            console.log(e);
            e.preventDefault();
            //var mobile = this.state.data.user.mobile;
            var files = $('#userPic')[0].files;
            var formData = new FormData();
            formData.append('imgFile', files[0]);
            if(files.length > 0){
                if(WebUtil.isAllowedPic(files[0].type, files[0].size)){
                    var url = "/customer/pic/upload";

                    var success = function(res){
                        console.log(res);
                        if(res.errCode == 0){
                            toastr.options = {
                                "positionClass": "toast-top-center",
                                "hideDuration": "10000",
                            };
                            var msg = '<div><button type="button" id="cancelBtn" class="btn btn-primary">取消</button>' +
                                '<button type="button" id="sureBtn" class="btn" style="margin: 0 8px 0 8px">更换</button></div>';
                            var $toast = toastr.warning(msg, "是否更换头像？");
                            if ($toast.find('#cancelBtn').length) {
                                $toast.delegate('#cancelBtn', 'click', function () {
                                    $toast.remove();
                                });
                            }
                            if ($toast.find('#sureBtn').length) {
                                $toast.delegate('#sureBtn', 'click', function () {
                                    var url = "/customer/pic/change?headImg="+res.url;
                                    var successFunc = function(result){
                                        toastr.success("修改成功！");
                                        $('#userImage').attr("src", res.url);
                                    }.bind(this);
                                    ajaxGet(url,successFunc);
                                });
                            }

                        } else{
                            toastr.error(res.msg);
                        }

                    }.bind(this);

                    $.ajax({
                        url: url,
                        type: 'POST',
                        data: formData,
                        processData: false,//用来回避jquery对formdata的默认序列化，XMLHttpRequest会对其进行正确处理
                        contentType: false,//设为false才会获得正确的conten-Type
                        async: true,
                        success: success,
                        error: function(xhr,status,err){
                            console.log(xhr,status,err.toString());
                            toastr.error(err);
                        }

                    });
                }
                else{
                    toastr.warning("请选择大小在1M以内的图片文件!!");
                }
            }
        },

        submitInfo:function(){
            var gen = $('input:radio:checked').val()
            var phone = $("#myPhone").val();
            var nickName = $("#nickName").val();
            var bir = $("#myBir").val();
            if(gen ==  undefined) gen = "";
            var data={phone:phone,birthday:bir,gen:parseInt(gen),nickName:nickName}
            global.actions.personalAction.changeUserInfo(data,this)
        },
        render:function(){
            var activeHeaderClassSet = React.addons.classSet({
                activeHeader: true
            });
            var self=this;
            var sex=this.state.data.userInfo.sex==1?"男":"女";
            var headImg=this.state.data.userInfo.headImg==""?"/assets/images/head.png":this.state.data.userInfo.headImg;

            return(
                <header>
                    <nav className="navbar navbar-default">
                        <div className="container-fluid">

                            <div className="navbar-header">
                                <a className="navbar-brand" href="#">
                                    <img src="/assets/images/brand.png" alt="brand picture"/>
                                </a>
                            </div>

                            <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                                <ul className="nav navbar-nav">
                                    <li>
                                        <Link to="allSave" activeClassName={activeHeaderClassSet}>全部收藏</Link>
                                    </li>
                                    <li>
                                        <Link to="personalSave" activeClassName={activeHeaderClassSet}>个人收藏</Link>
                                    </li>
                                    <li>
                                        <Link to="task" activeClassName={activeHeaderClassSet}>我的任务</Link>
                                    </li>
                                    <li>
                                        <Link to="comment" activeClassName={activeHeaderClassSet}>与我相关</Link>
                                    </li>
                                </ul>

                                <ul className="nav navbar-nav navbar-right">
                                    <li><a onClick={this.infoToggle}>{$CONF$.nickName || $CONF$.email}</a>
                                    </li>
                                    <li><a onClick={this.openModal}>修改密码</a>
                                    </li>
                                    <li><a className="" href="/logout">退出</a>
                                    </li>
                                    <div className="collapse" id="collapseInfo">
                                        <div className="well">
                                            <div id="personalMessage-header">
                                                <li style={{paddingTop:'5px'}}>
                                                    <span className="symbol">头像</span>
                                                    <div className="form-group" style={{position:"relative",float:"right"}}>
                                                        <input type="file" id="userPic"  onChange={this.handleUserPicChange}/>
                                                        <img id="userImage" src={headImg} style={{height:'50px',position:"absolute",top:-15,right:0}} alt=""/>
                                                    </div>
                                                    <hr style={{marginTop:"20px"}}/>
                                                </li>
                                                <li>
                                                    <span className="symbol">昵称</span>
                                                    <span style={{marginLeft:'50px'}}>{this.state.data.userInfo.nickName}</span>
                                                    <hr/>
                                                </li>
                                                <li>
                                                    <span className="symbol">性别</span>
                                                    <span style={{marginLeft:'50px'}}>{sex}</span>
                                                    <hr/>
                                                </li>
                                                <li>
                                                    <span className="symbol">电话</span>
                                                    <span style={{marginLeft:'50px'}}>{this.state.data.userInfo.phone}</span>
                                                    <hr/>
                                                </li>
                                                <li>
                                                    <span className="symbol">生日</span>
                                                    <span style={{marginLeft:'50px'}}>{this.state.data.userInfo.birthday}</span>
                                                    <hr/>
                                                </li>
                                                <li>
                                                   <i className="fa fa-pencil-square-o" onClick={this.openInfoModal}></i>
                                                </li>
                                            </div>
                                        </div>
                                    </div>
                                </ul>

                            </div>
                        </div>
                    </nav>
                    <BootstrapModalPc ref="changePassword" id="changePassword" title="修改密码" onConfirm={this.onConfirm}>
                        <div className="loginForm">
                            <div className="input-group">
                                <input type="password" className="form-control" id="oldPassword" placeholder="原密码（8-20位）"onBlur={this.judgeOldPwd} aria-describedby="basic-addon1"/>
                                <div className="tips" id="oldPass" style={{display:"block"}}>
                                    <p style={{color:"#818A91",marginBottom:"1",display:self.state.data.passFlag==1?"block":"none"}}>原密码正确</p>
                                    <p style={{display:self.state.data.passFlag==2?"block":"none"}}>原密码错误（8-20位）</p>

                                </div>
                            </div>
                            <div className="input-group">
                                <input type="password" className="form-control" id="passwordInput" placeholder="新密码（8-20位）"onBlur={this.judgeStrong} aria-describedby="basic-addon1"/>
                                <div className="tips" id="passP">
                                    <p>密码格式有误（8-20位）</p>
                                </div>
                            </div>
                            <div className="input-group">
                                <input type="password" className="form-control" id="confirmInput" placeholder="确认新密码"onBlur={this.judgeSame} aria-describedby="basic-addon1"/>
                                <div className="tips" id="confirmP">
                                    <p>密码不一致</p>
                                </div>
                            </div>

                        </div>
                    </BootstrapModalPc>
                    <BootstrapModalPc ref="changeInfo" id="changeInfo" title="修改信息" onConfirm={this.submitInfo}>
                        <span style={{color: 'darkolivegreen', fontWeight: 'bold', fontSize: '16px'}}>昵称: </span>
                        <input ref="nickName" id="nickName" name="nickName" ></input>
                        <hr/>
                        <span style={{color: 'darkolivegreen', fontWeight: 'bold', fontSize: '16px'}}>性别:</span>
                        <input  name="gender" type="radio" value={1}>男</input>
                        <input  name="gender" type="radio" value={2}>女</input>
                        <hr/>
                        <span style={{color: 'darkolivegreen', fontWeight: 'bold', fontSize: '16px'}}>电话: </span>
                        <input ref="myPhone" id="myPhone" name="phone" ></input>
                        <hr/>
                        <span style={{color: 'darkolivegreen', fontWeight: 'bold', fontSize: '16px'}}>生日: </span>
                        <input ref="myBir" id="myBir" name="birthday" type="date" ></input>

                    </BootstrapModalPc>
                </header>

            )
        }
    });

    components.AllSave = React.createClass({
        render:function(){
            return(
                <div className="container">
                    <div className="row" style={{padding:"15px"}}>
                        <button className="btn btn-success" onClick={this.openModal}>
                            <i className="fa fa-plus"></i>
                            <span>添加收藏</span>
                        </button>
                    </div>
                    <div className="row">
                        <div className="col-md-3">
                            <div className="panel panel-default">
                                <div className="panel-heading">
                                    <h3 className="panel-title">用户推荐</h3>
                                </div>
                                <div className="panel-body">用户</div>
                            </div>
                        </div>
                        <div className="col-md-6">
                            <div className="panel panel-default">
                                <div className="panel-heading">
                                    <h3 className="panel-title" style={{display:"inline"}}>我的收藏</h3>
                                    <a href="" style={{float:"right"}}>×</a>
                                </div>
                                <div className="panel-body">
                                    <p>用户</p>
                                    <div>

                                    </div>
                                </div>
                                <div className="panel-footer">
                                    <div>
                                        <p>
                                            <i className="fa fa-commenting" aria-hidden="true"></i>
                                            <span>评论</span>
                                        </p>
                                        <p>
                                            <i className="fa fa-thumbs-up" aria-hidden="true"></i>
                                            <span>点赞</span>
                                        </p>
                                        <p>
                                            <i className="fa fa-share" aria-hidden="true"></i>
                                            <span>转发</span>
                                        </p>
                                        <p>
                                            <i className="fa fa-trash" aria-hidden="true"></i>
                                            <span>删除</span>
                                        </p>
                                    </div>
                                    <form className="form-inline">
                                        <div className="form-group">
                                            <div className="input-group">
                                                <input type="text" className="form-control" id="searchInput" placeholder="请输入评论内容"
                                                       style={{width: '470px'}}
                                                       onKeyDown={this.handleKeyDown}/>
                                                <div  className="input-group-addon" >确认</div>
                                            </div>

                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <div className="col-md-3">
                            <div className="panel panel-default">
                                <div className="panel-heading">
                                    <h3 className="panel-title">收藏推荐</h3>
                                </div>
                                <div className="panel-body">收藏</div>
                            </div>
                        </div>
                    </div>

                </div>
            )
        }
    });

    components.PersonalSave = React.createClass({
        mixins:[
            Reflux.connect(stores.PersonalStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],

        componentWillMount:function(){
            global.actions.personalAction.getPersonalSave()
        },
        onConfirm:function(){
            var url = $("#url").val();
            var description = $("#description").val();
            var secret = $('input:radio:checked').val()
            var data={url:url,description:description,secret:parseInt(secret),label:this.state.data.labelNum}
            if(url=="" || secret==undefined || this.state.data.labelNum==0){
                toastr.warning("必填项有空");return;
            }
            global.actions.personalAction.addSave(data,this)
        },
        openModal:function(){
            this.refs.addSave.open();
        },
        choseLabel:function(num){
            global.actions.personalAction.choseLabel(num)
        },
        handleKeyDown:function(evt){
            if(evt.which === 13){
                //this.searchUser(evt);
            } else if(evt.which === 27){
                $("#searchInput").val("").focus();
            }
        },
        closePanel:function(index){
            var id="#save"+index;
            $(id).css({display:"none"})
        },
        deleteSave:function(id,index){
            var self =this;
            swal({
                title: "确定要删除吗?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消"
            }, function () {
                global.actions.personalAction.deletePersonalSave(id,index)
            });
        },
        render:function(){

            return(
              <div className="container">
                  <div className="row" style={{padding:"15px"}}>
                      <button className="btn btn-success" onClick={this.openModal}>
                          <i className="fa fa-plus"></i>
                          <span>添加收藏</span>
                      </button>
                  </div>
                  <div className="row">
                      <div className="col-md-3">
                          <div className="panel panel-default">
                              <div className="panel-heading">
                                  <h3 className="panel-title">用户推荐</h3>
                              </div>
                              <div className="panel-body">用户</div>
                          </div>
                      </div>
                      <div className="col-md-6" >
                          {this.state.data.saveList.map(function(e, index){
                              var time = WebUtil.timeFormat(e.insertTime)
                              var labelTitle = "";
                              switch(e.label){
                                  case 1: labelTitle="学习";break;
                                  case 2: labelTitle="娱乐";break;
                                  case 3: labelTitle="资料";break;
                                  case 4: labelTitle="购物";break;
                                  case 5: labelTitle="其他";break;
                              }
                              return(
                                  <div className="panel panel-default" id={"save"+index}>
                                      <div className="panel-heading">
                                          <h3 className="panel-title" style={{display:"inline"}}>我的收藏</h3>
                                          <a onClick={this.closePanel.bind(this,index)} style={{float:"right",cursor:"pointer"}}>×</a>
                                      </div>
                                      <div className="panel-body">
                                          <p>{$CONF$.nickName}</p>
                                          <div style={{height:"25px"}}>
                                              <span>{time}</span>
                                              <span style={{marginLeft:"20px"}} className="label label-success">{labelTitle}</span>
                                          </div>
                                          <p>链接：<a href="">{e.url}</a></p>
                                          <p>内容：{e.content}</p>
                                      </div>
                                      <div className="panel-footer">
                                          <div>
                                              <p>
                                                  <i className="fa fa-commenting" aria-hidden="true"></i>
                                                  <span>评论</span>
                                              </p>
                                              <p>
                                                  <i className="fa fa-thumbs-up" aria-hidden="true"></i>
                                                  <span>点赞</span>
                                              </p>
                                              <p>
                                                  <i className="fa fa-share" aria-hidden="true"></i>
                                                  <span>转发</span>
                                              </p>
                                              <p onClick={this.deleteSave.bind(this,e.id,index)}>
                                                  <i className="fa fa-trash" aria-hidden="true"></i>
                                                  <span>删除</span>
                                              </p>
                                          </div>
                                          <form className="form-inline">
                                              <div className="form-group">
                                                  <div className="input-group">
                                                      <input type="text" className="form-control" id="searchInput" placeholder="请输入评论内容"
                                                             style={{width: '470px'}}
                                                             onKeyDown={this.handleKeyDown}/>
                                                      <div  className="input-group-addon" >确认</div>
                                                  </div>

                                              </div>
                                          </form>
                                      </div>
                                  </div>

                              )
                          }.bind(this))}
                      </div>

                      <div className="col-md-3">
                          <div className="panel panel-default">
                              <div className="panel-heading">
                                  <h3 className="panel-title">收藏推荐</h3>
                              </div>
                              <div className="panel-body">收藏</div>
                          </div>
                      </div>
                  </div>
                  <BootstrapModalPc ref="addSave" id="addSave" title="添加收藏" onConfirm={this.onConfirm}>
                      <div className="input-group">
                          <div className="form-group">
                              <label for="nameCh"> 网址(必填以http/https开头的绝对网址)</label>
                              <input type="text" className="form-control" id="url"
                                     name="url" maxLength="200" placeholder=""/>
                              <p className="help-block"></p>
                          </div>
                          <div className="form-group">
                              <label for="nameCh"> 简介(选填)</label>
                              <input type="text" className="form-control" id="description"
                                     name="description" maxLength="200" placeholder=""/>
                              <p className="help-block"></p>
                          </div>
                          <div className="form-group">
                              <label for="nameCh"> 标签(必填)</label>
                              <br/>
                              <span className={this.state.data.labelNum==1?"label label-success":"label label-default"} onClick={this.choseLabel.bind(this,1)}>学习</span>
                              <span className={this.state.data.labelNum==2?"label label-success":"label label-default"} onClick={this.choseLabel.bind(this,2)}>娱乐</span>
                              <span className={this.state.data.labelNum==3?"label label-success":"label label-default"} onClick={this.choseLabel.bind(this,3)}>资料</span>
                              <span className={this.state.data.labelNum==4?"label label-success":"label label-default"} onClick={this.choseLabel.bind(this,4)}>购物</span>
                              <span className={this.state.data.labelNum==5?"label label-success":"label label-default"} onClick={this.choseLabel.bind(this,5)}>其他</span>
                          </div>
                          <div className="form-group">
                              <p><input type="radio" name="secretOption" value={1} />私密</p>
                              <p><input type="radio" name="secretOption" value={2} />公开</p>
                          </div>
                      </div>
                  </BootstrapModalPc>
              </div>

            )
        }
    });

    components.Tasks = React.createClass({
        mixins:[
            Reflux.connect(stores.TaskStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        componentWillMount(){
            global.actions.taskAction.getAllTask();
        },
        keyDown: function(evt){
            var text = this.refs.taskInput.getDOMNode().value;
            if(evt.which === 13 && text){
                evt.preventDefault();
                this.submitNewTask();
            }
            else if(evt.which === 27){
                this.refs.taskInput.getDOMNode().value = '';
                this.refs.taskInput.getDOMNode().blur();
            }
        },
        submitNewTask: function(){
            var taskText = $.trim(React.findDOMNode(this.refs.taskInput).value);
            var state = parseInt($("#stateId").val())
            if(!taskText){
                toastr.info("请输入内容");
                return;
            }
            var data={content:taskText,state:state}
            this.refs.taskInput.getDOMNode().value = '';
            global.actions.taskAction.addTask(data);
        },
        toggleEdit: function(index) {
            $("#edit"+index).toggle("slow");
        },
        changeStatus:function(id,index){
            var state = parseInt($("#state"+index).val());
            var oldState = this.state.data.taskList[index].state;
            if(state ==oldState) toastr.info("Are u kidding? 任务状态没有发生改变");
            else if(state<oldState) toastr.info("任务状态不能回退!");
            else global.actions.taskAction.changeTaskState(id,state,index)
        },
        deleteTask:function(id,index){
            swal({
                title: "确定要删除吗?",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消"
            }, function () {
                global.actions.taskAction.deleteTask(id,index)
            });
        },
        handleClickState:function(){
            var state = parseInt($("#state_name").val());
            global.actions.taskAction.getListByState(state)
        },
        render:function(){
            return(
                <div>
                    <div className="container">
                        <div className="container-fluid">
                            <div className="row">
                                <div className="col-md-9 col-sm-12 col-xs-12" style={{paddingRight: '20px'}}>
                                    <div className="page-header">
                                        <h1>
                                            <small>新建task</small>
                                        </h1>
                                    </div>
                                    <div id="task-state" style={{marginBottom:"10px"}}>
                                        <h> 任务状态：</h>
                                        <select ref="state" className="form-control" style={{display: 'inline-block', width: '100px'}} name="statename" id="stateId">
                                            <option value={1}>未开始</option>
                                            <option value={2}>进行中</option>
                                            <option value={3}>完成</option>
                                        </select>
                                    </div>

                                    <textarea ref="taskInput" className="form-control col-xs-12" onKeyDown={this.keyDown} rows="5" placeholder="你做了什么？" id="newtask" name="description"></textarea>
                                    <button className="btn btn-inverse" id="submit-task" onClick={this.submitNewTask} style={{marginTop:"10px"}}>提交</button>
                                    <br/>
                                    <hr/>
                                    <br/>
                                    <div className="page-header">
                                        <span className="personal-change"> 任务动态 </span>
                                        <select className="form-control" name="state_name" id="state_name" onChange={this.handleClickState}
                                                style={{display: 'inline', maxWidth: '10em'}}>
                                            <option value={0} data-toggle="tooltip" data-placement="top" title="">全部</option>
                                            <option value={1} data-toggle="tooltip" data-placement="top" title="">未开始</option>
                                            <option value={2} data-toggle="tooltip" data-placement="top" title="">进行中</option>
                                            <option value={3} data-toggle="tooltip" data-placement="top" title="">已完成</option>
                                        </select>
                                    </div>


                                    <ul className="tasks" id="tasks">
                                        {
                                            this.state.data.taskList.map(function(task,index){
                                                var state = "";
                                                switch(task.state){
                                                    case 1:state="未开始";break;
                                                    case 2:state="进行中";break;
                                                    case 3:state="已完成";break;
                                                    case 4:state="废弃";break;
                                                }
                                            return (
                                                <div className="task-content">
                                                    <div className="left-state">
                                                        <img id="nowPic" src="/assets/images/head.png" className="header" style={{ display: 'block',height: '30px',width:'30px',float:'left'}}/>
                                                        <span style={{marginLeft:'15px'}} />
                                                        <span className="author_name" >
                                                            <Link to="/">
                                                                {$CONF$.nickName || $CONF$.email}
                                                            </Link>
                                                        </span>
                                                        <span className="task_state"> {state} </span>
                                                        <span className="event_label pushed"> {task.content} </span>
                                                        <div style={{display: 'inline', marginLeft: '10px', fontSize: '15px'}}>
                                                            <div className="state-edit"  data-toggle="tooltip" data-placement="top" title="单击编辑按钮进行编辑"
                                                                 onClick={this.toggleEdit.bind(this,index)}>
                                                                <span className="glyphicon glyphicon-edit"></span>
                                                            </div>
                                                            <div ref="editTask" className="drop-down" id={"edit"+index}>
                                                                <span className="edit-statetext"> 修改状态： </span>
                                                                <select ref="taskState" className="edit-state"  id={"state"+index}name="edit-statename" autoFocus>
                                                                    <option value={1}> 未开始 </option>
                                                                    <option value={2}> 进行中 </option>
                                                                    <option value={3}> 已完成 </option>
                                                                    <option value={4}> 废弃 </option>
                                                                </select>
                                                                <div className="btn-group btn-edit-state" >
                                                                    <button className="btn btn-info btn-sm close-state" onClick={this.toggleEdit.bind(this,index)}> 关闭 </button>
                                                                    <button className="btn btn-info btn-sm summit-state" onClick={this.changeStatus.bind(this,task.id,index)}> 提交 </button>
                                                                    <button className="btn btn-success btn-sm delete-state" onClick={this.deleteTask.bind(this,task.id,index)}> 删除当前task </button>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div className="right-info">
                                                        <div className="date" data-toggle="tooltip" data-placement="top" >
                                                            {WebUtil.timeFormat(task.insertTime)}</div>
                                                    </div>
                                                </div>
                                            )
                                        }.bind(this))
                                        }
                                    </ul>
                                    <ul id="nomore">
                                        {
                                            //noMore
                                        }
                                    </ul>
                                </div>
                                <div className="col-md-3 col-sm-0 col-xs-0" style={{borderLeft: '1px solid #e7e7e7'}} id="teamProjectList">
                                    <h1 style={{paddingLeft: '15px',borderBottom:"1px solid #999"}}>
                                        <small>提醒</small>
                                    </h1>
                                    <h3>未开始</h3>
                                    <ul className="projects" id="projects" style={{paddingLeft: '20px'}}>
                                        {
                                            //this.state.taskList.projects.map(function(project){
                                            //    return(
                                            //        <li>
                                            //            <div className="event-title thumbnail" id="thumbnail9" data-toggle="tooltip" data-placement="top" title={"简介：" + project.description + ", 创建于 " + RheaUtil.timeFormat(project.ctime)}>
                                            //                <span className="project_name" style={{display: 'inline-block', width: '90%'}}>
                                            //                    <Link to="projectState" params={{projectId: project.id}} style={{display: 'block', width: '100%'}}>
                                            //                        {project.name}
                                            //                    </Link>
                                            //                </span>
                                            //                <span className="glyphicon glyphicon-menu-right" style={{display: 'inline-block', width: '10%', float: 'none', marginTop: '-1px'}}></span>
                                            //            </div>
                                            //        </li>
                                            //    )
                                            //})
                                        }
                                    </ul>
                                    <h3>进行中</h3>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            )
        }
    });
    components.Comment = React.createClass({
        mixins:[
            Reflux.connect(stores.contentStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        render:function(){

            return(
                <div style={{with:"100px",height:"100px",background:"red"}}>

                </div>

            )
        }
    });


}(window.React, window.ReactRouter, window.Reflux, window));