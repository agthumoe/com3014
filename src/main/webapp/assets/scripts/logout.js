$('document').ready(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $('#logout').click(function () {
        $.ajax({
            type: 'POST',
            url: '/logout',
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function () {
                window.location.replace("/account/login?logout");
            }
        });
    });
});
