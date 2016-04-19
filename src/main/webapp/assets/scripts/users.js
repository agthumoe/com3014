$('document').ready(function () {
    var deleteModal = $('#delete-modal');
    var notification = $('#notification');
    deleteModal.modal({show: false});
    notification.hide();

    (function populateUsersTable() {
        $.ajax({
            url: '/api/users?page=0&size=10&sort=id,asc',
            type: 'GET',
            success: function (data) {
                var tbody = $('#user-table tbody');
                tbody.html("");

                $.each(data.pagedList, function (k, v) {
                    var status = v.enabled ? '<i class="fa fa-unlock-alt success"></i>' : '<i class="fa fa-lock danger"></i>';
                    var role = v.admin ? 'Admin' : 'User';

                    var trashButton = $('<a />').attr({
                            href: '#',
                            'data-target-id': v.id
                        })
                        .addClass('btn btn-danger btn-xs btn-delete')
                        .append($('<i />')
                            .addClass('fa fa-trash'))
                        .click(function () {
                            console.log("btn-delete was clicked");
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
                                        // refresh the page or clear and reload the table
                                        populateUsersTable();
                                    }
                                });
                            });
                            deleteModal.modal('show');
                        });

                    var editButton = $('<a />').attr({
                            href: '/admin/users/' + v.id,
                        })
                        .addClass('btn btn-warning btn-xs')
                        .append($('<i />').addClass('fa fa-pencil'));

                    tbody.append(
                        $('<tr />').append(
                            '<th>' + v.id + '</th>' +
                            '<td>' + v.username + '</td>' +
                            '<td>' + v.createdDate + '</td>' +
                            '<td>' + v.lastModifiedDate + '</td>' +
                            '<td>' + v.email + '</td>' +
                            '<td>' + v.name + '</td>' +
                            '<td>' + status + '</td>' +
                            '<td>' + role + '</td>'
                        ).append(
                            $('<td />').addClass('text-center')
                                .append(trashButton)
                                .append('&nbsp;')
                                .append(editButton)
                        )
                    );
                });
            }
        });
    }) ();
});
