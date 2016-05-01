
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    var stores = global.stores;
    var actions = global.actions;
    components.Header = React.createClass({
        mixins:[
            Reflux.connect(stores.contentStore,"data"),
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
            Reflux.connect(stores.contentStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        render:function(){
            var activeHeaderClassSet = React.addons.classSet({
                activeHeader: true
            });
            return(
                <header>
                </header>

            )
        }
    });

    components.Tasks = React.createClass({
        mixins:[
            Reflux.connect(stores.contentStore,"data"),
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
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>我的任务</Link>
                                    </li>
                                    <li>
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>与我相关</Link>
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
    components.Comment = React.createClass({
        mixins:[
            Reflux.connect(stores.contentStore,"data"),
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
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>我的任务</Link>
                                    </li>
                                    <li>
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>与我相关</Link>
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


}(window.React, window.ReactRouter, window.Reflux, window));