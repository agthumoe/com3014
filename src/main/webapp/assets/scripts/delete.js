$(document).ready(function () {
    var deleteModal = $('#delete-modal');
    var notification = $('#notification');
    deleteModal.modal({show: false});
    notification.hide();
    $(".btn-delete").click(function () {
        var id = $(this).data("target-id");
        var btnDelete = $('#btn-delete-confirm');
        btnDelete.click(function () {
            $.ajax({
                url: '/api/users/' + id,
                type: 'DELETE',
                success: function () {
                    deleteModal.modal("hide");
                    $('#notification-message').html("User has been deleted!");
                    notification.show();
                    notification.fadeTo(3000, 500).slideUp(500, function () {
                        notification.alert('close');
                    });
                    $('#user-' + id).remove();
                }
            });
        });
        console.log("User: " + id + " has been deleted");
        deleteModal.modal('show');
    });
});
