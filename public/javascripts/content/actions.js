
(function(Reflux, global) {

    global.actions={};
    global.actions.contentAction = Reflux.createActions([
        'sendEmail',
        'setPassWord',
        'setFindState',
        'resetPassword',
    ]);


}(window.Reflux, window));