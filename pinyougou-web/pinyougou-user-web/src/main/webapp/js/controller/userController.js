<<<<<<< HEAD
/** 定义控制器层 */
app.controller('userController', function($scope, $timeout, baseService){

    // 定义user对象
    $scope.user = {};
    /** 用户注册 */
    $scope.save = function () {
        // 判断两次密码是否一致
        if ($scope.okPassword && $scope.user.password == $scope.okPassword){
            // 发送异步请求
            baseService.sendPost("/user/save?code=" + $scope.code, $scope.user)
                .then(function(response){
                if (response.data){
                    // 清空表单数据
                    $scope.user = {};
                    $scope.okPassword = "";
                    $scope.code = "";
                }else{
                    alert("注册失败！");
                }
            });

        }else{
            alert("两次密码不一致！");
        }
    };




    // 发送短信验证码
    $scope.sendSmsCode = function () {
        // 判断手机号码
        if ($scope.user.phone && /^1[3|4|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendSmsCode?phone=" + $scope.user.phone)
                .then(function(response){
                if (response.data){
                    // 调用倒计时方法
                    $scope.downcount(90);

                }else{
                    alert("发送失败！");
                }
            });
        }else {
            alert("手机号码格式不正确！")
        }
    };


    $scope.smsTip = "获取短信验证码";
    $scope.disabled = false;

    // 倒计时方法
    $scope.downcount = function (seconds) {

        seconds--;

        if (seconds >= 0){
            $scope.smsTip = seconds + "秒后，重新获取！";
            $scope.disabled = true;
            // 第一个参数：回调的函数
            // 第二个参数：间隔的时间毫秒数
            $timeout(function(){
                $scope.downcount(seconds);
            }, 1000);
        }else {
            $scope.smsTip = "获取短信验证码";
            $scope.disabled = false;
        }

    };

=======
/** 定义控制器层 */
app.controller('userController', function($scope, $timeout, baseService){

    // 定义user对象
    $scope.user = {address:{}};
    /** 用户注册 */
    $scope.save = function () {

        // 判断两次密码是否一致
        if ($scope.okPassword && $scope.user.password == $scope.okPassword){
            // 发送异步请求
            baseService.sendPost("/user/save?code=" + $scope.code, $scope.user)
                .then(function(response){
                if (response.data){
                    // 清空表单数据
                    $scope.user = {};
                    $scope.okPassword = "";
                    $scope.code = "";
                }else{
                    alert("注册失败！");
                }
            });

        }else{
            alert("两次密码不一致！");
        }
    };




    // 发送短信验证码
    $scope.sendSmsCode = function () {

        // 判断手机号码
        if ($scope.user.phone && /^1[3|4|5|7|8]\d{9}$/.test($scope.user.phone)){
            // 发送异步请求
            baseService.sendGet("/user/sendSmsCode?phone=" + $scope.user.phone)
                .then(function(response){
                if (response.data){
                    // 调用倒计时方法
                    $scope.downcount(90);

                }else{
                    alert("发送失败！");
                }
            });
        }else {
            alert("手机号码格式不正确！")
        }
    };


    $scope.smsTip = "获取短信验证码";
    $scope.disabled = false;

    // 倒计时方法
    $scope.downcount = function (seconds) {

        seconds--;

        if (seconds >= 0){
            $scope.smsTip = seconds + "秒后，重新获取！";
            $scope.disabled = true;
            // 第一个参数：回调的函数
            // 第二个参数：间隔的时间毫秒数
            $timeout(function(){
                $scope.downcount(seconds);
            }, 1000);
        }else {
            $scope.smsTip = "获取短信验证码";
            $scope.disabled = false;
        }

    };
	
	
	/*获取所有的省*/
    $scope.getProvinces = function (){
        baseService.sendGet("/user/findProvinces").then(function (response) {
            if (response.data){
                $scope.provinces = response.data;
            }else {
                alert("操作失误")
            }
        })
    };

    /*根据省份查询城市*/
    $scope.$watch("user.address.province",function (newValue,oldValue) {
        /*初始化城市集合*/
        $scope.citiesList = [];
        if (newValue){
            baseService.sendGet("/user/findCities","provinceId="+ newValue).then(function (response) {
                if (response.data){
                    $scope.citiesList = response.data
                }
            })
        }
    });

    /*根据城市查询区域*/
    $scope.$watch("user.address.city",function (newValue,oldValue) {
        /*初始化地域集合*/
        $scope.areasList = [];
        if (newValue){
            baseService.sendGet("/user/findAreas","cityId="+ newValue).then(function (response) {
                if (response.data){
                    $scope.areasList = response.data
                }
            })
        }
    });

    /*查询用户信息*/
    $scope.getUser = function () {
        baseService.sendGet("/user/getUser").then(function (response) {
            if (response.data){

                $scope.user = response.data.user;

                /*用户如果没有设置头像,则使用默认头像*/
                if ($scope.user.headPic == null){
                    $scope.user.headPic = "img/_/photo.png";
                }
                /*转换为JOSN格式*/
                $scope.user.address = JSON.parse(response.data.user.address);
                $scope.user.birthday = response.data.birthday;
            }else {
                alert("获取用户信息失败!")
            }
        })
    };

    /*修改用户头像*/
    $scope.updateHeadPic = function () {
        /*调用服务层上传文件*/
        baseService.uploadFile().then(function (response) {
            /*获取响应数据 */
            if (response.data.status == 200){
                $scope.user.headPic = response.data.url;
                $scope.updateUser();
            }else {
                alert("上传失败!")
            }
        });
    };

    /*修改用户信息设置*/
    $scope.updateUser = function () {
        baseService.sendPost("/user/updateUser",$scope.user).then(function (response) {
            if(response.data){
                alert("保存成功!");
                $scope.getUser();
            }else {
                alert("保存失败!");
            }
        })
    }
>>>>>>> f75dacd6a7dce102264a931ec541a2a9ee717f59
});