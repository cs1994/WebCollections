
(function(React, ReactRouter, Reflux, global) {
    var Link = ReactRouter.Link;
    global.components = {};
    var stores = global.stores;
    var actions = global.actions;
    components.FirstPage = React.createClass({
        render:function(){
            return(
                <div style={{with:"100px",height:"100px",background:"red"}}>

                </div>
            )
        }
    });


}(window.React, window.ReactRouter, window.Reflux, window));