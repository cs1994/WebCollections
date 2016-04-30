
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
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>商户</Link>
                                    </li>
                                    <li>
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>用户</Link>
                                    </li>
                                    <li>
                                        <Link to="forgetPassword" activeClassName={activeHeaderClassSet}>餐厅</Link>
                                    </li>

                                </ul>

                                <ul className="nav navbar-nav navbar-right">
                                    <li><a className="" href="javascript:;">{$CONF$.nickName || $CONF$.email}</a></li>
                                    <li><a className="" href="/admin/logout">退出</a></li>
                                </ul>
                            </div>
                        </div>
                    </nav>
                </header>

            )
        }
    });

    components.FirstPage = React.createClass({
        render:function(){
            return(
                <div style={{with:"100px",height:"100px",background:"red"}}>

                </div>
            )
        }
    });


}(window.React, window.ReactRouter, window.Reflux, window));