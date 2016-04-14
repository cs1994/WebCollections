
(function(Reflux, global) {

    global.actions={};
    global.actions.registerAction = Reflux.createActions([
        'sendEmail',
        'setPassWord',
        'resetPassword',
    ]);


}(window.Reflux, window));