
(function(Reflux, global) {

    global.actions={};
    global.actions.allAction = Reflux.createActions([
        'getAllSave',
        'submitComment',
        'replyComment',
        'deleteSave',
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
        'replyComment',
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
    global.actions.searchAction = Reflux.createActions([
        'getAllComments',

    ]);


}(window.Reflux, window));