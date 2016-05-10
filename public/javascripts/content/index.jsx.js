
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
            <DefaultRoute handler={Components.AllSave}/>
            <Route name="allSave" path="/allSave" handler={Components.AllSave}/>
            <Route name="personalSave" path="/personalSave" handler={Components.PersonalSave}/>
            <Route name="task" path="/task" handler={Components.Tasks}/>
            <Route name="comment" path="/relate" handler={Components.Comment}/>
            <Route name="search" path="/search" handler={Components.Search}/>
        </Route>
    );
    Router.run(routes, function(Handler) {
        React.render(<Handler />, document.body);
    });

}(window.React, window.ReactRouter, window.Reflux, window.components));