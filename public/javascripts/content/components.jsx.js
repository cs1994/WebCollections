
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    var stores = global.stores;
    var actions = global.actions;
    components.Header = React.createClass({
        mixins:[
            //Reflux.connect(stores.contentStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        render:function(){
            var activeHeaderClassSet = React.addons.classSet({
                activeHeader: true
            });
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
                                    <li><a className="" href="javascript:;">{$CONF$.nickName || $CONF$.email}</a></li>
                                    <li><a className="" href="/logout">退出</a></li>
                                </ul>
                            </div>
                        </div>
                    </nav>
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