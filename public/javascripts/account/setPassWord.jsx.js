(function(React,Router,Reflux,Components){
    //var Route = Router.Route;
    //var DefaultRoute = Router.DefaultRoute;
    //var NotFoundRoute = Router.NotFoundRoute;
    //var RouteHandler = Router.RouteHandler;
    //var Link = Router.Link;
    var SetPassWord= Components.SetPassWord
    React.render(
        <SetPassWord />,
        document.getElementById('webSave')
    );
}(window.React, window.ReactRouter, window.Reflux, window.components));