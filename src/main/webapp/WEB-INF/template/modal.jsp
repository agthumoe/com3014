<%--
  Author: Aung Thu Moe
  Date: 06/04/2016
  Time: 17:39
  Required Attributes: modal_id, title, message, confirm_button_id, confirm_button_label
--%>
<div class="modal fade" id="${param.modal_id}" tabindex="-1" role="dialog" aria-labelledby="modal-label">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                    aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="modal-label">${param.title}</h4>
            </div>
            <div class="modal-body">
                ${param.message}
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="${param.btn_confirm_id}">${param.btn_confirm_label}</button>
            </div>
        </div>
    </div>
</div>
