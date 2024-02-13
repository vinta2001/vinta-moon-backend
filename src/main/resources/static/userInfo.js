import crypto from 'crypto-js'
window.onload = function () {
    $(".shell").mouseleave(function(){
        $(".login-tips").css("display","none")
    })
    $(".username").mouseleave(function () {
        let reg = /^([a-zA-Z0-9]+)(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/
        let username = $(".username").val()
        if (username.length !== 0) {
            let reuslt = reg.test(username)
            if (!reuslt) {
                $("#username-tips").css("display", "block")
            } else {
                $("#username-tips").css("display", "none")
            }
        }
    })
}

function md5(str){
    return crypto.MD5(str).toString()
}

