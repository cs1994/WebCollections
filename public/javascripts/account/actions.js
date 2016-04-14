
(function(Reflux, global) {

    global.actions={};
    global.actions.registerAction = Reflux.createActions([
        'sendEmail',
        'setPassWord',
        'setFindState',
        'resetPassword',
    ]);


}(window.Reflux, window));