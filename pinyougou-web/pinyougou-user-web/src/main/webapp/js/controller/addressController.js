app.controller('addressController', function ($scope, baseService) {

    /*查询用户信息和收件地址*/
    $scope.getUserAndAddress = function () {
        baseService.sendGet("/address/getUserAndAddress").then(function (response) {
            if (response.data){
                $scope.user = response.data.user;
                /*用户如果没有设置头像,则使用默认头像*/
                if ($scope.user.headPic == null){
                    $scope.user.headPic = "img/_/photo.png";
                }
                $scope.addressList =  response.data.address;
                /*收件人电话中间4位*号代替*/
                for (var i = 0;i <  $scope.addressList.length;i++){
                    var mobile = $scope.addressList[i].mobile;
                    $scope.addressList[i].mobile = mobile.substring(0, 3) + "****" + mobile.substring(7,11)
                }
            }else {
                alert("获取用户信息失败!")
            }
        })
    };

    /*添加收件地址*/
    $scope.addOrUpdateAddress = function () {
        var url = "save"; /* 添加URL*/
        /* 判断id是否存在*/
        if ($scope.address.id){
            url = "update"; /* 修改URL*/
        }
        baseService.sendPost("/address/" + url,$scope.address).then(function (response) {
            if (response.data){
                $scope.getUserAndAddress();
            }else {
                alert("操作失败")
            }
        })
    };


    /*获取所有的省*/
    $scope.getProvinces = function (){
        baseService.sendGet("/address/findProvinces").then(function (response) {
            if (response.data){
                $scope.provinces = response.data;
            }else {
                alert("操作失误")
            }
        })
    };

    /*根据省份查询城市*/
    $scope.$watch("address.provinceId",function (newValue,oldValue) {
        /*初始化城市集合*/
        $scope.citiesList = [];
        if (newValue){
            baseService.sendGet("/address/findCities","provinceId="+ newValue).then(function (response) {
                if (response.data){
                    $scope.citiesList = response.data
                }
            })
        }
    });

    /*根据城市查询区域*/
    $scope.$watch("address.cityId",function (newValue,oldValue) {
        /*初始化地域集合*/
        $scope.areasList = [];
        if (newValue){
            baseService.sendGet("/address/findAreas","cityId="+ newValue).then(function (response) {
                if (response.data){
                    $scope.areasList = response.data
                }
            })
        }
    });

    /*编辑按钮点击事件*/
    $scope.show = function (address) {
        // 把address转化成json字符串
        var jsonStr = JSON.stringify(address);
        // 把address字符串转化成json对象
        $scope.address = JSON.parse(jsonStr);
    };

    /*删除地址*/
    $scope.delete = function (id) {
        if (confirm("确认删除吗?")){
            baseService.sendGet("/address/delete","id="+id).then(function (response) {
                if (response){
                    $scope.getUserAndAddress();
                }else {
                    alert("操作失败!")
                }
            })
        }
    };

    /*设定默认地址*/
    $scope.changeDefault = function (id) {
        if (confirm("确认要设为默认吗?")){
            baseService.sendGet("/address/changeDefault","id="+id).then(function (response) {
                if (response){
                    $scope.getUserAndAddress();
                }else {
                    alert("操作失败!")
                }
            })
        }
    }
});