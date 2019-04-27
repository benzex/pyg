/** 定义控制器层 */
app.controller('indexController', function ($scope, baseService) {
    /** 定义获取登录用户名方法 */
    $scope.showName = function () {
        baseService.sendGet("/user/showName")
            .then(function (response) {
                $scope.loginName = response.data.loginName;
            });
    };
    $scope.rows = 1;
    $scope.getOrderByUserId = function (page) {
        if (page < 1) {
            page = 1;
        }
        if ($scope.totalPage != null) {
            if (page > $scope.totalPage) {
                page = $scope.totalPage;
            }
        }
        baseService.sendGet("/order/getOrderByUserId?page=" + page + "&rows=" + $scope.rows)
            .then(function (response) {
                $scope.orderList = response.data.rows;
                $scope.totalPage =
                    response.data.total % $scope.rows == 0 ? response.data.total / $scope.rows : parseInt(response.data.total / $scope.rows) + 1;
                $scope.page = page;
                $scope.pageInfo($scope.totalPage);
            })
    };

    $scope.pageInfo = function (totalPage) {
        $scope.pageNum = [];
        var start = 0;
        var end = totalPage;
        $scope.firstD = false;
        $scope.endD = false;
        if (totalPage > 5) {
            if ($scope.page > 3) {
                start = $scope.page - 3;
                $scope.firstD = true;
                if ($scope.page < totalPage - 2) {
                    end = $scope.page + 2;
                    $scope.endD = true;
                } else {
                    start = totalPage - 5;
                }
            } else {
                end = 5;
                $scope.endD = true;
            }
        }

        for (var i = start; i < end; i++) {
            $scope.pageNum.push(i + 1);
        }


    }


});