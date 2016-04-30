
(function(React,Router,Reflux,Components){
    var Route = Router.Route;
    var DefaultRoute = Router.DefaultRoute;
    var NotFoundRoute = Router.NotFoundRoute;
    var RouteHandler = Router.RouteHandler;
    var Link = Router.Link;

    var App = React.createClass({
        render: function(){
            return(
                <div>
                    <Components.Header></Components.Header>
                    <RouteHandler />
                </div>
            )
        }
    });

    var routes = (
        <Route handler={App} path="/">
            <DefaultRoute handler={Components.FirstPage}/>
            <Route name="forgetPassword" path="/forgetPassword" handler={Components.FirstPage}/>
        </Route>
    );
    Router.run(routes, function(Handler) {
        React.render(<Handler />, document.body);
    });

}(window.React, window.ReactRouter, window.Reflux, window.components));