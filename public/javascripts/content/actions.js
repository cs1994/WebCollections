
(function(Reflux, global) {

    global.actions={};
    global.actions.personalAction = Reflux.createActions([
        'choseLabel',
        'addSave',
        'getPersonalSave',
        'deletePersonalSave',
        'checkOldPass',
        'changePass',
    ]);


}(window.Reflux, window));