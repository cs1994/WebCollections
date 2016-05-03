
(function(Reflux, global) {

    global.actions={};
    global.actions.personalAction = Reflux.createActions([
        'choseLabel',
        'addSave',
        'getPersonalSave',
        'deletePersonalSave',
        'checkOldPass',
        'changePass',
        'changeUserInfo',
        'getuserInfo',
    ]);
    global.actions.taskAction = Reflux.createActions([
        'addTask',
    ]);


}(window.Reflux, window));