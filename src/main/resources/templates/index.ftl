<h1>扫一下该二维码，实现扫码登陆</h1>

<img src="getCodeImg?token=${token}">
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
    function checkToken() {
        // alert('1000');
        $.ajax({
            url: "checkToken?token=${token}",
            dataType: "json",
            type: "get",
            async: "false",
            success: function (data) {
                if (data == true) {
                    window.location.href = '/sweepCode';
                }
            },
            error: function () {

            }
        });
    }
    setInterval(checkToken, 2000);

</script>
