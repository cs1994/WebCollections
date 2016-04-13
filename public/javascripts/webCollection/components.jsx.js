
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    var stores = global.stores;
    var actions = global.actions;
    components.Register = React.createClass({
        mixins:[
            Reflux.connect(stores.registerStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        componentDidMount:function(){

        },
        submit:function(){
            var email = $("#emailInput").val()
            if(WebUtil.isEmail(email)){
                actions.registerAction.sendEmail(email)
            }
        },
        render:function(){
            return(
                <div className="register">
                    <div className="registerHeader">
                       <div style={{width:"50%",display:"inline-block"}}>
                           <p style={{borderRight:"3px solid #777"}}>收藏</p>
                           <p>注册</p>
                       </div>
                        <div style={{width:"50%",display:"inline-block",textAlign:"right"}}>
                            <p style={{fontSize:"18px"}}>已有账号？</p>
                            <button>登录</button>
                        </div>
                    </div>
                    <div className="registerContent">
                        <p > 请输入您要注册的邮箱</p>
                        <input type="text" id="emailInput" placeholder="邮箱"/>
                        <button onClick={this.submit} >同意协议并注册</button>
                    </div>
                </div>
            )
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));