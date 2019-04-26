app.controller('settingController', function($scope,$timeout ,$controller,baseService){
    $controller('userController',{$scope:$scope});

    $scope.loadSateUI=function(){
        baseService.sendPost("/setting/loadSateUI").then(
            function (response) {
                $scope.user = response.data;
            }
        )
    };
    $scope.getPhone=function(phone){
        var phone = phone+'';
        var str = '';
        for (var i = 0; i < 11;i++){
            if ( i< 3 || i>6){
                str += phone[i]
            }else {
                str += '*';
            }
        }
        return str;
    };
    $scope.updateUser = function () {
        if ($scope.newPassword == $scope.okPassword) {
            $scope.user.password = $scope.newPassword;
            baseService.sendPost("/setting/update", $scope.user).then(
                function (response) {
                    if (response.data) {
                        alert("修改成功,请重新登陆!!!");
                        location.href = '/logout';
                    } else {
                        alert("更改失败")
                    }
                }
            )
        }else {
            alert("两次输入密码不一致!!");
        }
    };
    $scope.newPhone='';
    $scope.updatePhone = function () {
        if (/^1[34578]\d{9}$/.test($scope.newPhone)){
            alert($scope.smsCode+" : " + $scope.code);
            if (/^[1-9]\d{5}$/.test($scope.smsCode) && /^\w{4}$/.test($scope.code)){
                $scope.user.phone = $scope.newPhone;
                    baseService.sendPost("/setting/updatePhone?code="+$scope.code+"&smsCode="+$scope.smsCode,$scope.user).then(
                    function (response) {
                        if (response.data){
                            $scope.code='';
                            $scope.smsCode='';
                            location.href="home-setting-address-complete.html";
                        }else {
                            alert("验证码错误");
                            $scope.code='';
                            $scope.smsCode='';
                        }
                    }
                );
            }else {
                alert("请输入正确格式的验证码!!")
            }
        }else {
            alert("请输入正确格式的手机号!!!")
        }
    };
    $scope.verifyPhone = function () {
        alert($scope.smsCode+" : " + $scope.code);
        if (/^[1-9]\d{5}$/.test($scope.smsCode) && /^\w{4}$/.test($scope.code)){
            baseService.sendGet("/setting/verifyPhone?code="+$scope.code+"&phone="+$scope.user.phone+"&smsCode="+$scope.smsCode).then(
                function (response) {
                    if (response.data){
                        $scope.code='';
                        location.href = "home-setting-address-phone.html";
                    }
                    else {
                        alert("请输入正确的验证码");
                        $scope.code='';
                        $scope.smsCode='';
                    }
                }
            );
        }else {
            alert("情输入正确格式的验证码")
        }
    };
});