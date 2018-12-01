/*
    author:jitwxs
    time:2018-03-15
 */

/*
    功能：发送JSON串的AJAX
    _type：请求类型：get|post|put|delete
    _url：请求路径
    _data：json串
    _async：是否开启异步
    _successCallback：执行成功回调方法
    _errorCallback：执行失败回调方法
 */
function sendJson(_type, _url, _data, _async, _successCallback, _errorCallback) {
    $.ajax({
        type: _type,
        async: _async,
        url: _url,
        dataType: 'json',
        data: _data,
        success: function (msg) {
            _successCallback(msg);
        },
        error: function (error) {
            _errorCallback(error);
        }
    });
}

/*
    功能：上传文件的AJAX
    _url：请求路径
    _data：formData对象
    _async：是否开启异步
    _successCallback：执行成功回调方法
    _errorCallback：执行失败回调方法
 */
function sendFile(_url, _data, _async, _successCallback, _errorCallback) {
    $.ajax({
        type: "post",
        async: _async,
        url: _url,
        dataType: 'json',
        // 告诉jQuery不要去处理发送的数据
        processData : false,
        // 告诉jQuery不要去设置Content-Type请求头
        contentType: false,
        data: _data,
        success: function (msg) {
            _successCallback(msg);
        },
        error: function (error) {
            _errorCallback(error);
        }
    });
}