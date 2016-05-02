
(function(Reflux, global) {

    global.actions={};
    global.actions.personalAction = Reflux.createActions([
        'choseLabel',
        'addSave',
        'getPersonalSave',
        'deletePersonalSave',
    ]);


}(window.Reflux, window));