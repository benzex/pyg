app.controller("orderController", function ($scope, $controller, $location, baseService, $interval) {

    $controller('baseController', {$scope: $scope});

    $scope.getOrderId = function () {
        $scope.orderId = $location.search().orderId;
        $scope.payCode();
    };

    $scope.payCode = function () {

        baseService.sendGet("/order/payCode?orderId=" + $scope.orderId).then(function (response) {
            $scope.outTradeNo = response.data.outTradeNo;
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            $scope.codeUrl = response.data.codeUrl;
            document.getElementById("qrcode").src = "/barcode?url=" + $scope.codeUrl;

            var timer = $interval(function () {
                baseService.sendGet("/order/queryPayStatus?outTradeNo="
                    + $scope.outTradeNo).then(function (response) {

                    if (response.data.status == 1) {
                        $interval.cancel(timer);
                        location.href = "/order/paysuccess.html?money=" + $scope.money;
                    }
                    if (response.data.status == 3) {

                        $interval.cancel(timer);

                        location.href = "/order/payfail.html";
                    }
                });
            }, 3000, 100);

            timer.then(function () {

                $scope.tip = "二维码已过期，刷新页面重新获取二维码。";
            });
        })
    }
});