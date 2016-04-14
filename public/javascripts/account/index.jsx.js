
(function(React,Router,Reflux,Components){
    var Route = Router.Route;
    var DefaultRoute = Router.DefaultRoute;
    var NotFoundRoute = Router.NotFoundRoute;
    var RouteHandler = Router.RouteHandler;
    var Link = Router.Link;

    var App = React.createClass({
        render: function(){
            return(
                <RouteHandler />
            )
        }
    });

    var routes = (
        <Route handler={App} path="/">
            <DefaultRoute handler={Components.Register}/>
            <Route name="postCarInfo" path="/postCarInfo" handler={Components.Register}/>
        </Route>
    );
    Router.run(routes, function(Handler) {
        React.render(<Handler />, document.getElementById('webSave'));
    });
}(window.React, window.ReactRouter, window.Reflux, window.components));