
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    toastr.options = {
        "positionClass": "toast-top-center",
        "hideDuration": "10000",
    };
    var stores = global.stores;
    var actions = global.actions;
    components.RegisterHeader = React.createClass({
        gotoLogin:function(){
            location.href="/login"
        },
        render:function(){
            return(
                <div className="registerHeader">
                    <div style={{width:"50%",display:"inline-block"}}>
                        <p style={{borderRight:"3px solid #777"}}>收藏</p>
                        <p>注册</p>
                    </div>
                    <div style={{width:'50%',display:"inline-block",textAlign:"right"}}>
                        <p style={{fontSize:"18px"}}>已有账号？</p>
                        <button onClick={this.gotoLogin}>登录</button>
                    </div>
                </div>
            )
        }
    });

    components.Register = React.createClass({
        mixins:[
            Reflux.connect(stores.registerStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        submit:function(){
            var email = $("#emailInput").val()
            if(WebUtil.isEmail(email)){
                console.log("@@@@@@@@@@@@@ " + email)
                actions.registerAction.sendEmail(email)
            }
            else toastr.warning("请输入正确的邮箱！")
        },
        loginEmail:function(){
            var emailType = this.state.data.email.split("@")[1].split(".")[0]
            var url = WebUtil.findfullemail(emailType)
            window.open(url);
        },
        render:function(){
            var dom = this.state.data.state == 0 ?
                <div className="registerContent">
                    <p > 请输入您要注册的邮箱</p>
                    <input type="text" id="emailInput" placeholder="邮箱"/>
                    <button onClick={this.submit} >同意协议并注册</button>
                </div>
                :
                <div className="registerContent">
                    <p > 一封邮件已经发送到您的：{this.state.data.email}邮箱</p>
                    <button onClick={this.loginEmail}>马上验证</button>
                </div>
            return(
                <div className="register">
                    <components.RegisterHeader />
                    {dom}
                </div>
            )
        }
    });

    components.SetPassWord = React.createClass({
        mixins:[
            Reflux.connect(stores.registerStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        judgeName:function(){
            var name=$("#nameInput").val();
            if(name=="")$("#nameP").css("display","block");
            else $("#nameP").css("display","none")
        },
        judgeStrong:function(){
            var passWord=$("#passwordInput").val()
            if(7<passWord.length && passWord.length<21) $("#passP").css("display","none");
            else $("#passP").css("display","block");
        },
        judgeSame:function(){
            var passWord1=$("#passwordInput").val()
            var passWord2=$("#confirmInput").val()
            if(passWord1 == passWord2) $("#confirmP").css("display","none");
            else $("#confirmP").css("display","block");

        },
        submit:function(){
            var tokenOpt = WebUtil.GetQueryString("token");
            var token = tokenOpt? tokenOpt:"none";
            var name=$("#nameInput").val();
            var passWord1=$("#passwordInput").val()
            var passWord2=$("#confirmInput").val()
            if(name=="" || passWord1.length<8 || passWord1.length>20 || passWord1!=passWord2){
                toastr.warning("请根据提示填写")
            }
            else{
                var data={name:name,password:passWord1,token:token}
                actions.registerAction.setPassWord(data)
            }
        },
        render:function(){
            return(
                <div className="register">
                    <components.RegisterHeader />
                    <div className="registerContent">
                        <p> 设置用户名和密码</p>
                        <div style={{marginBottom:"20px"}}>
                            <p style={{display:"inline-block",width:"80px",textAlign:"right",margin:0}}>用户名</p>
                            <input type="text" id="nameInput" placeholder="用户名" style={{display:"inline-block"}}
                               onBlur={this.judgeName} />
                            <p id="nameP"style={{margin:0,display:"none",fontSize:"14px"}}>用户名不能为空</p>
                        </div>
                        <div style={{marginBottom:"20px"}}>
                            <p style={{display:"inline-block",width:"80px",textAlign:"right",margin:0}}>登录密码</p>
                            <input type="text" id="passwordInput" type="password" placeholder="登陆密码（8-20位）" style={{display:"inline-block"}}
                             onBlur={this.judgeStrong}   />
                            <p id="passP"style={{margin:0,display:"none",fontSize:"14px"}}>密码格式有误（8-20位）</p>
                        </div>
                        <div style={{marginBottom:"20px"}}>
                            <p style={{display:"inline-block",width:"80px",textAlign:"right",margin:0}}>确认密码</p>
                            <input type="text" id="confirmInput" type="password" placeholder="确认密码" style={{display:"inline-block"}}
                                   onBlur={this.judgeSame} />
                            <p id="confirmP" style={{margin:0,display:"none",fontSize:"14px"}}>密码不一致</p>
                        </div>
                        <button style={{marginLeft:"40px"}} onClick={this.submit}>确认</button>
                    </div>
                </div>
            )
        }
    });

    components.ForgetPassword = React.createClass({
        mixins:[
            Reflux.connect(stores.registerStore,"data")],
        componentWillMount(){
            actions.registerAction.setFindState($TAG$)
        },
        resetPassword:function(){
            var email = $("#emailInput").val()
            if(WebUtil.isEmail(email)){
                //console.log("!!!!!!!!!!!!! " +email)
                actions.registerAction.resetPassword(email)
            }
            else
                toastr.warning("请输入正确的邮箱")
        },
        judgeStrong:function(){
            var passWord=$("#passwordInput").val()
            if(7<passWord.length && passWord.length<21) $("#passP").css("display","none");
            else $("#passP").css("display","block");
        },
        judgeSame:function(){
            var passWord1=$("#passwordInput").val()
            var passWord2=$("#confirmInput").val()
            if(passWord1 == passWord2) $("#confirmP").css("display","none");
            else $("#confirmP").css("display","block");

        },
        loginEmail:function(){
            var emailType = this.state.data.email.split("@")[1].split(".")[0]
            var url = WebUtil.findfullemail(emailType)
            window.open(url);
        },
        submitNewPassword(){
            var tokenOpt = WebUtil.GetQueryString("token");
            var token = tokenOpt? tokenOpt:"none";
            var passWord1=$("#passwordInput").val()
            var passWord2=$("#confirmInput").val()
            if(passWord1.length<8 || passWord1.length>20){
                toastr.warning("密码位数不正确")
            }
            else if(passWord1!=passWord2){toastr.warning("密码输入不一致")}
            else{
                var data={password:passWord1,token:token}
                var url = "/customer/reset/password/email"
                ajaxPost(url,data,function(res){
                    toastr.success("重置成功");
                    location.href="/login"
                })
                //actions.registerAction.setPassWord(data)
            }
        },
        gotoLogin:function(){
            location.href="/login"
        },
        render:function(){
            var Dom= null;
                switch(this.state.data.findState){
                    case 1:
                        Dom=
                        <div className="registerContent">
                            <p> 请输入您的邮箱</p>
                            <input type="text" id="emailInput" placeholder="邮箱"/>
                            <br/>
                            <span>此功能将会发送一个重置密码的邮件到输入的邮箱，即可重置密码。</span>
                            <br/>
                            <button onClick={this.resetPassword} style={{marginTop:"20px"}}>发送邮件</button>
                        </div>
                        break;
                    case 2:
                        Dom=
                            <div className="registerContent">
                                <p > 一封邮件已经发送到您的：{this.state.data.email}邮箱</p>
                                <button onClick={this.loginEmail}>马上验证</button>
                            </div>;
                        break;
                    case 3:
                        Dom=
                            <div className="registerContent">
                                <div style={{marginBottom:"20px"}}>
                                    <p style={{display:"inline-block",width:"80px",textAlign:"right",margin:0}}>登录密码</p>
                                    <input type="text" id="passwordInput" type="password" placeholder="登陆密码（8-20位）" style={{display:"inline-block"}}
                                           onBlur={this.judgeStrong}   />
                                    <p id="passP"style={{margin:0,display:"none",fontSize:"14px"}}>密码格式有误（8-20位）</p>
                                </div>
                                <div style={{marginBottom:"20px"}}>
                                    <p style={{display:"inline-block",width:"80px",textAlign:"right",margin:0}}>确认密码</p>
                                    <input type="text" id="confirmInput" type="password" placeholder="确认密码" style={{display:"inline-block"}}
                                           onBlur={this.judgeSame} />
                                    <p id="confirmP" style={{margin:0,display:"none",fontSize:"14px"}}>密码不一致</p>
                                </div>
                                <button onClick={this.submitNewPassword} style={{marginTop:"20px"}}>确认</button>
                            </div>
                        break;

                }
            return(
                <div className="register">
                    <div className="registerHeader">
                        <div style={{width:"50%",display:"inline-block"}}>
                            <p style={{borderRight:"3px solid #777"}}>收藏</p>
                            <p>找回密码</p>
                        </div>
                        <div style={{width:'50%',display:"inline-block",textAlign:"right"}}>
                            <p style={{fontSize:"18px"}}>已有账号？</p>
                            <button onClick={this.gotoLogin}>登录</button>
                        </div>
                    </div>
                    {Dom}
                </div>
            )
        }
    });

    components.Login = React.createClass({
        mixins:[
            Reflux.connect(stores.registerStore,"data"),
            ReactRouter.Navigation,
            ReactRouter.State],
        emailLogin:function(){

            console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            var loginForm = $(".form-group");
            var email = loginForm.find("input[name=account]").val().trim();
            var pwd = loginForm.find("input[name=password]").val();
            console.log("@@@@@@@@@@@@@@@@@@@@@ " + pwd)
            if(WebUtil.isEmail(email)){
                if(pwd){
                    var data = {account:email,password:pwd}
                    var url ="/login/submit";
                    ajaxJsonPostTwo(url,data, function (res) {
                        console.log("登录成功")
                        location.href="/#/allSave";
                    },function(){toastr.warning("账户不存在或密码不正确")})
                }
                else toastr.warning("请输入密码")
            }
            else toastr.warning("请输入正确的邮箱")
        },
        render:function(){
            return(
                <div id="login-app">
                    <div className="loginPage">
                        {
                            //<div className="logo">
                            //    <img style={{height: '100%',width: '100%'}} src="/assets/images/logo.png"/>
                            //</div>
                        }

                        <div className="tongtu">
                        </div>
                        <div className="formArea">
                            <div>
                                <div style={{float: 'left',width: '30%'}}>
                                    <p className="title">用户登录</p>
                                </div>

                            </div>


                                <div className="form-group">
                                    <input name="account" type="text" className="form-control" placeholder="邮箱" autofocus />
                                    <input name="password" type="password" className="form-control" placeholder="密码" />
                                    <a href="/forgetPassword">忘记密码？</a>
                                    <button className="loginBtn" onClick={this.emailLogin}>登录账号</button>
                                </div>
                            <p>还没有账号？<a href="/register">免费注册</a></p>
                            <br/>
                        </div>
                    </div>
                </div>
            )
        }
    });

}(window.React, window.ReactRouter, window.Reflux, window));