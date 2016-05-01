$('document').ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var deleteModal = $('#delete-modal');
    deleteModal.modal({show: false});

    function paginate(response) {
        $('#pagination').pagination({
            totalPage: response.totalPages,
            callback: function (currentPage) {
                populateUsersTable(currentPage - 1);
            }
        });
    }

    function notify(message, type) {
        type = type.toLowerCase();
        var notification = $('<div />').addClass("alert alert-dismissible top-right notification alert-" + type);
        notification.append("<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>&times;</span></button>");
        var icon = $('<i />').addClass('fa').attr('aria-hidden', true);
        if (type === 'success') {
            icon.addClass("fa-check-circle");
        } else if (type === 'info') {
            icon.addClass("fa-info-circle");
        } else if (type === 'warning') {
            icon.addClass("fa-exclamation-circle");
        } else if (type === 'danger') {
            icon.addClass("fa-times-circle");
        } else {
            console.error("Invalid type argument: " + type);
        }
        notification.append(icon).append("&nbsp;<span>" + message + "</span>");
        $('#page-wrapper').append(notification);
        notification.show();
        notification.fadeTo(3000, 500).slideUp(500, function () {
            notification.alert('close');
            notification.remove();
        });
    }

    function queryUsers(url, callback) {
        $.ajax({
            url: url,
            type: 'GET',
            success: function (response) {
                var tbody = $('#user-table tbody');
                tbody.html("");
                var list = response.hasOwnProperty('pagedList') ? response.pagedList : response;
                $.each(list, function (k, v) {
                    var status = v.enabled ? '<i class="fa fa-check success"></i>' : '<i class="fa fa-ban danger"></i>';
                    var role = $('<td />').addClass("text-center");
                    if (v.admin) {
                        role.append('<span class="primary">Admin</span>');
                    } else {
                        role.append('<span class="warning">User</span>');
                    }

                    var trashButton = $('<a />').attr({
                            href: '#',
                            'data-target-id': v.id
                        })
                        .addClass('btn btn-danger btn-xs btn-delete')
                        .append($('<i />')
                            .addClass('fa fa-trash'))
                        .click(function () {
                            var id = $(this).data("target-id");
                            var btnDelete = $('#btn-delete-confirm');
                            btnDelete.unbind().click(function () {
                                $.ajax({
                                    url: '/api/users/' + id,
                                    type: 'DELETE',
                                    beforeSend: function (xhr) {
                                        xhr.setRequestHeader(header, token);
                                    },
                                    success: function () {
                                        deleteModal.modal("hide");
                                        notify("User has been deleted!", "info");
                                        // refresh the page or clear and reload the table
                                        queryUsers(url, callback);
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
                                '<td class="text-center">' + status + '</td>'
                            )
                            .append(role)
                            .append(
                                $('<td />').addClass('text-center')
                                    .append(trashButton)
                                    .append('&nbsp;')
                                    .append(editButton)
                            )
                    );
                });
                $('#pagination').html("");
                if (typeof callback !== 'undefined') {
                    callback(response);
                }
            },
            error: function (response) {
                notify(response.responseJSON.message, "danger");
            }
        });
    }

    function populateUsersTable(pageNumber) {
        var paginationUrl = '/api/users?page=' + pageNumber + '&size=10&sort=id,asc';
        queryUsers(paginationUrl, paginate);
    }

    function filter() {
        var filterBy = $('#filter_by option:selected').val();
        var filter = $('#filter_type').val();
        var limit = $('#filter_limit').val();
        var url = '/api/users/filter?filterBy=' + filterBy + '&filter=' + filter + '&limit=' + limit;
        if (filter != '') {
            queryUsers(url);
        } else {
            populateUsersTable(0);
        }
    }

    $('#btn-filter').click(function () {
        var filterBy = $('#filter_by option:selected').val();
        var filter = $('#filter_type').val();
        var limit = $('#filter_limit').val();
        var url = '/api/users/filter?filterBy=' + filterBy + '&filter=' + filter + '&limit=' + limit;
        queryUsers(url);
    });

    $('#filter_type').keyup(function (e) {
        filter();
    });
    $('#btn-reset-filter').click(function () {
        populateUsersTable(0);
        $('#filter_by option:selected').val("username");
        $('#filter_type').val("");
        $('#filter_limit').val(10);
    });

    populateUsersTable(0);
});
