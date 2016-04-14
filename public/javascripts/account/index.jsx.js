
(function(React,Router,Reflux,Components){
    //var Route = Router.Route;
    //var DefaultRoute = Router.DefaultRoute;
    //var NotFoundRoute = Router.NotFoundRoute;
    //var RouteHandler = Router.RouteHandler;
    //var Link = Router.Link;

    //var App = React.createClass({
    //    render: function(){
    //        return(
    //            <RouteHandler />
    //        )
    //    }
    //});
    //
    //var routes = (
    //    <Route handler={App} path="/">
    //        <DefaultRoute handler={Components.Register}/>
    //        <Route name="forgetPassword" path="/forgetPassword" handler={Components.ForgetPassword}/>
    //    </Route>
    //);
    //Router.run(routes, function(Handler) {
    //    React.render(<Handler />, document.getElementById('webSave'));
    //});

        var Register= Components.Register
        React.render(
            <Register />,
            document.getElementById('webSave')
        );
}(window.React, window.ReactRouter, window.Reflux, window.components));