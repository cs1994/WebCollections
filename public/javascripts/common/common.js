var BootstrapModalPc=React.createClass({
    propTypes: {
        id: React.PropTypes.number.isRequired
    },
    open:function(){
        $(this.getDOMNode()).modal('show');
    },
    close:function(){
        $(this.getDOMNode()).modal('hide');
    },
    confirm:function(){
        this.props.onConfirm();
    },
    render:function(){
        return(
            <div className="modal fade">
                <div className="modal-dialog">
                    <div className="modal-content" style={{margin:'auto',width:'500px'}}>
                        <div className="modal-header">
                            <a className="close" data-dismiss="modal">×</a>
                            <h3>{this.props.title}</h3>
                        </div>
                        <div className="modal-body">
                            {this.props.children}
                        </div>
                        <div className="modal-footer">
                            <a className="btn btn-default" onClick={this.close}>取消</a>
                            <a className="btn btn-success" onClick={this.confirm}>确定</a>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
});
