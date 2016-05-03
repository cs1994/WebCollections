
(function(Reflux, global) {

    global.actions={};
    global.actions.allAction = Reflux.createActions([
        'getAllSave',
    ]);
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
        'getAllTask',
        'changeTaskState',
        'deleteTask',
        'getListByState',
        'getUnstart',
    ]);


}(window.Reflux, window));