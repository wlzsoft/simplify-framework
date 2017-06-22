<html>
<head>
</head>
<body>
<script type="text/javascript">
function callback(data) {
    alert(data.value);
}
$(function(){
    $(".download-excel").on("click",function(e) {
        var iframe = document.createElement("iframe");
        iframe.src = "/disk/testDownloadFile.stream?model=${paramModel}&callback=parent.window.callback&script=1";
        iframe.style.display = "none";
        document.body.appendChild(iframe);
    });
});
</script>
</body>
</html>