/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };
    $scope.rows = 2;
    $scope.getOrderByUserId = function (page) {
        if (page<1){
            page = 1;
        }

        if ($scope.totalPage!=null){
            if (page >$scope.totalPage){
                page = $scope.totalPage;
            }
        }

        baseService.sendGet("/order/getOrderByUserId?page="+page +"&rows="+ $scope.rows)
            .then(function (response) {
            $scope.orderList = response.data.rows;
                $scope.totalPage =
                    response.data.total%$scope.rows == 0?response.data.total/$scope.rows:parseInt(response.data.total/$scope.rows)+1;
            $scope.pageInfo($scope.totalPage);
            $scope.page=page;
        })
    };

    $scope.pageNum = [];

    $scope.pageInfo =function (totalPage) {

        for (var i = 0; i < totalPage; i++) {
            $scope.pageNum.push(i+1);
        }


    }
});