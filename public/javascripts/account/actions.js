
(function(Reflux, global) {

    global.actions={};
    global.actions.registerAction = Reflux.createActions([
        'sendEmail',
        'setPassWord',
    ]);


}(window.Reflux, window));