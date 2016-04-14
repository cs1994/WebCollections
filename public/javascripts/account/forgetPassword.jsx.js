(function(React,Router,Reflux,Components){
    //var Route = Router.Route;
    //var DefaultRoute = Router.DefaultRoute;
    //var NotFoundRoute = Router.NotFoundRoute;
    //var RouteHandler = Router.RouteHandler;
    //var Link = Router.Link;
    var ForgetPassword= Components.ForgetPassword
    React.render(
        <ForgetPassword />,
        document.getElementById('webSave')
    );
}(window.React, window.ReactRouter, window.Reflux, window.components));