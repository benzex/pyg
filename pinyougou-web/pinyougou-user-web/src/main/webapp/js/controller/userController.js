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
	
	
	/*获取所有的省 城市 地区*/
    $scope.getProvinces = function () {
        baseService.sendGet("/user/getAddress").then(function (response) {
            if (response.data){
                $scope.provinces = response.data.provinces;
                $scope.cities = response.data.cities;
                $scope.areas = response.data.areas;
            }else {
                alert("操作失误")
            }
        })
    };

    /*根据省份查询城市*/
    $scope.$watch("user.address.province",function (newValue,oldValue) {
        if (newValue){
            var provinces = $scope.provinces;
            for (var i = 0;i < provinces.length;i++){
                if(newValue == provinces[i].province){
                    var provinceId = provinces[i].provinceId;
                    break;
                }
            }
            $scope.citiesList = [];
            for (var i = 0;i <$scope.cities.length;i++){
                if($scope.cities[i].provinceId == provinceId){
                    $scope.citiesList.push($scope.cities[i]);
                }
            }
        }
    });

    /*根据城市查询区域*/
    $scope.$watch("user.address.city",function (newValue,oldValue) {
        if (newValue){
            var cities = $scope.cities;
            for (var i = 0;i < cities.length;i++){
                if(newValue == cities[i].city){
                    var cityId = cities[i].cityId;
                    break;
                }
            }
            $scope.areasList = [];
            for (var i = 0;i <$scope.areas.length;i++){
                if($scope.areas[i].cityId == cityId){
                    $scope.areasList.push($scope.areas[i]);
                }
            }
        }
    });

});