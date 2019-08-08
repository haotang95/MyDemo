$(function () {
    $("#loginBtn").on("click", function () {
        alert("1");
        $.ajax({
            timeout: g_timeout,
            type: "post",
            dataType: "json",
            url: action_url + "/demo/setValue",
            data: {},
            success: function (data) {
                if (data == "success") {
                    $.alert(data);
                }
            },
            error: function () {
            }
        });
    });
});
