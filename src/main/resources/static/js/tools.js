var g_timeout = 10000;
var action_url = "http://localhost:8080";
var PLATFORM_WEIXIN = "Weixin";
var PLATFORM_APP = "App";

//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

//判断是否是微信浏览器的函数
function isWeiXin() {
//window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
    var ua = window.navigator.userAgent.toLowerCase();
//通过正则表达式匹配ua中是否含有MicroMessenger字符串
    if (ua.match(/MicroMessenger/i) == 'micromessenger') {
        return true;
    } else {
        return false;
    }
}

function getRootPath() {
    //获取当前网址，如： http://localhost:8088/test/test.jsp
    var curPath = window.document.location.href;
    //获取主机地址之后的目录，如： test/test.jsp
    var pathName = window.document.location.pathname;
    var pos = curPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8088
    var localhostPaht = curPath.substring(0, pos);
    //获取带"/"的项目名，如：/test
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}

//比较日期大小
function CompareDate(d1, d2) {
    return ((new Date(d1.replace(/-/g, "\/"))) > (new Date(d2.replace(/-/g, "\/"))));
}

//写cookies
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

//读取cookies 
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

//删除cookies 
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

/**
 * 判断是否是webview环境
 */
function openInWebview() {
    var ua = navigator.userAgent.toLowerCase()
    if (ua.match(/MicroMessenger/i) == 'micromessenger') { // 微信浏览器判断
        return false
    } else if (ua.match(/WeiBo/i) == "weibo") {
        return false
    } else if (ua.indexOf('android') > -1 || ua.indexOf('adr') > -1) {
        return true
    } else {
        if (ua.match(/Android/i) != null) {
            return ua.match(/browser/i) == null
        } else if (ua.match(/iPhone/i) != null) {
            return ua.match(/safari/i) == null
        } else {
            return (ua.match(/macintosh/i) == null && ua.match(/windows/i) == null)
        }
    }
}

/**
 * webview相关方法调用
 */
function setupWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
        return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'wvjbscheme://__BRIDGE_LOADED__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function () {
        document.documentElement.removeChild(WVJBIframe)
    }, 0)
}



/**
 * webview返回上一页
 */
function upPage() {
    setupWebViewJavascriptBridge(function (bridge) {
        /* Initialize your app here 所有与iOS交互的JS代码放这里！*/
        bridge.callHandler('popToPreView',
            {'resultCode': '1', 'errorMessage': '', 'responseObject': {popLevel: '1', needRefresh: '0', url: ''}},
            function callback(response) {
                console.log('JS got response', response);

            }
        )

    })
}

/**
 * webview隐藏返回按钮
 */
function hiddenBack() {
    setupWebViewJavascriptBridge(function (bridge) {
        /* Initialize your app here 所有与iOS交互的JS代码放这里！*/
        bridge.callHandler('navigationInfo',
            {'resultCode': '1', 'errorMessage': '', 'responseObject': {title: '结果', hiddenBack: '1'}},
            function callback(response) {
                console.log('JS got response', response);
            }
        )
    })
}

/**
 * 微信结果页面禁止返回按钮事件
 */
function wxBack() {
    pushHistory();

    window.addEventListener("popstate", function (e) {
        var btnArray = ['否', '是'];

        mui.confirm('您确定退出当前页面吗？', '提示', btnArray, function (e) {

            if (e.index == 1) {
                //首页点击返回,直接关闭网页
                WeixinJSBridge.call('closeWindow');

            } else {


            }

        })
    }, false);

    function pushHistory() {
        var state = {
            title: "title",
            url: "#"
        };
        window.history.pushState(state, state.title, state.url);
    }
}


//图片压缩
function compress(img) {
    //    用于压缩图片的canvas
    var canvas = document.createElement("canvas");
    var ctx = canvas.getContext('2d');
    //    瓦片canvas
    var tCanvas = document.createElement("canvas");

    var tctx = tCanvas.getContext("2d");
    var initSize = img.src.length;
    var width = img.width;
    var height = img.height;
    //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
    var ratio;
    if ((ratio = width * height / 4000000) > 1) {
        ratio = Math.sqrt(ratio); //取平方根
        width /= ratio;
        height /= ratio;
    } else {
        ratio = 1;
    }
    canvas.width = width;
    canvas.height = height;
    //        铺底色
    ctx.fillStyle = "#fff";
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    //如果图片像素大于100万则使用瓦片绘制
    var count;
    if ((count = width * height / 1000000) > 1) {
        count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
        //            计算每块瓦片的宽和高
        var nw = ~~(width / count);
        var nh = ~~(height / count);
        tCanvas.width = nw;
        tCanvas.height = nh;
        for (var i = 0; i < count; i++) {
            for (var j = 0; j < count; j++) {
                tctx.drawImage(img, i * nw * ratio, j * nh * ratio, nw * ratio, nh * ratio, 0, 0, nw, nh);
                ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
            }
        }
    } else {
        ctx.drawImage(img, 0, 0, width, height);
    }
    //进行最小压缩
    var ndata = canvas.toDataURL('image/jpeg', 0.1);
    //  var ndata = canvas.toDataURL("image/"+ext);
    console.log('压缩前：' + initSize + '压缩后：' + ndata.length + '压缩率：' + ~~(100 * (initSize - ndata.length) / initSize) + "%");
    tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
    return ndata;
}


//提醒app端清理缓存
setupWebViewJavascriptBridge(function (bridge) {
    /* Initialize your app here 所有与iOS交互的JS代码放这里！*/
    bridge.callHandler('cleanWebCache',
        {'webVersion': '1.0.0'},
        function callback(response) {
            console.log('JS got response', response);
        }
    )
})