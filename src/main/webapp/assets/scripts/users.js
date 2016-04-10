$('document').ready(function () {
    $.ajax({
        url: '/api/users?page=0&size=10&sort=id,asc',
        type: 'GET',
        success: function (data) {
            var table = $('#user-table tbody');
            $.each(data.pagedList, function (k, v) {
                var status = v.enabled ? '<i class="fa fa-unlock-alt success"></i>' : '<i class="fa fa-lock danger"></i>';
                var role = v.admin ? 'Admin' : 'User';
                table.append(
                    '<tr>' +
                    '<th>' + v.id + '</th>' +
                    '<td>' + v.username + '</td>' +
                    '<td>' + v.createdDate + '</td>' +
                    '<td>' + v.lastModifiedDate + '</td>' +
                    '<td>' + v.email + '</td>' +
                    '<td>' + v.name + '</td>' +
                    '<td>' + status + '</td>' +
                    '<td>' + role + '</td>' +
                    '<td class="text-right">' +
                        '<a href="#" class="btn btn-danger btn-xs btn-delete" data-target-id="' + v.id + '"><i class="fa fa-trash"></i></a> ' +
                        '<a href="/admin/users/' + v.id + '" role="button" class="btn btn-warning btn-xs"><i class="fa fa-pencil"></i>' +
                    '</td>' +
                '</tr>'
                )
                ;
            });
        }
    });
});
