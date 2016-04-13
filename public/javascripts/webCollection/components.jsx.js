
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
                <div>
                    <input type="text" id="emailInput" />
                    <button onClick={this.submit} style={{width:"40px",height:"40px"}}></button>
                </div>
            )
        }
    });
}(window.React, window.ReactRouter, window.Reflux, window));