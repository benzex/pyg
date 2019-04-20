// 定义购物车的控制器
app.controller('orderController', function ($scope, $controller, baseService) {

    // 继承cartController
    $controller('cartController', {$scope : $scope});

    // 获取收件人地址列表
    $scope.findAddressByUser = function () {
        // 发送异步请求
        baseService.sendGet("/order/findAddressByUser").then(function(response){
            // 获取响应数据
            $scope.addressList = response.data;

            // 获取默认的收件地址
            $scope.address = $scope.addressList[0];
        });
    };

    // 用户选择地址
    $scope.selectedAddress = function (item) {
        $scope.address = item;
    };

    // 判断是否为选中的收件地址
    $scope.isSelectedAddress = function (item) {
        return $scope.address == item;
    };

    // 定义数据封装的json对象
    $scope.order = {paymentType : '1'};

    // 支付方式选择
    $scope.selectPayType = function (payType) {
        $scope.order.paymentType = payType;
    };

    // 提交订单
    $scope.saveOrder = function () {
        // 设置收件人地址
        $scope.order.receiverAreaName = $scope.address.address;
        // 设置收件人手机号码
        $scope.order.receiverMobile = $scope.address.mobile;
        // 设置收件人
        $scope.order.receiver = $scope.address.contact;
        // 订单来源
        $scope.order.sourceType = "2";

        // 发送异步请求
        baseService.sendPost("/order/save", $scope.order).then(function(response){
            // 获取响应数据
            if (response.data){
                // 如果支付方式是在线支付
                if ($scope.order.paymentType == 1){
                    // 跳转到支付页面
                    location.href = "/order/pay.html";
                }else{
                    // 跳转到成功页面
                    location.href = "/order/paysuccess.html";
                }
            }else{
                alert("提交订单失败！");
            }
        });

    };



});