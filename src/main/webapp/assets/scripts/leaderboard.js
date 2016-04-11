$('document').ready(function () {
    $.ajax({
        url: '/api/leaderboard',
        type: 'GET',
        success: function (data) {
            var table = $('#leaderboard-table tbody');
            $.each(data, function (k, v) {
                table.append('<tr><td>' + v.user.name + '</td>' + '<td>' + v.wins +'</td>' + '<td>' + v.losses + '</td>' + '<td>' +v.ratio + '</td></tr>')
            });
        },
        error: function (message) {
            console.log(message);
        }
    });
});
