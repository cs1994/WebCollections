
(function(Reflux, global) {

    global.actions={};
    global.actions.allAction = Reflux.createActions([
        'getAllSave',
        'submitComment',
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
    global.actions.commentAction = Reflux.createActions([
        'getAllComments',
        'deleteComment',
        'setReplayInfo',
        'replyComment',
        'getAllReply',
    ]);


}(window.Reflux, window));