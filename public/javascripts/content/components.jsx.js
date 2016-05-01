
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
                <div style={{with:"100px",height:"100px",background:"red"}}>

                </div>
            )
        }
    });

    components.PersonalSave = React.createClass({
        mixins:[
            Reflux.connect(stores.PersonalStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        onConfirm:function(){

        },
        openModal:function(){
            this.refs.addSave.open();
        },
        choseLabel:function(num){
            global.actions.personalAction.choseLabel(num)
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
                      <div className="col-md-6">
                          <div className="panel panel-default">
                              <div className="panel-heading">
                                  <h3 className="panel-title">我的收藏</h3>
                              </div>
                              <div className="panel-body">用户</div>
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