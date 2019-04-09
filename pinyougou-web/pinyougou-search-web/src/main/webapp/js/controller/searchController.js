/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope, baseService) {

    // 搜索参数
    $scope.searchParam = {keywords : ''};

    // 搜索方法
    $scope.search = function () {
        // 发送异步请求
        baseService.sendPost("/Search", $scope.searchParam).then(function (response) {
            // 获取响应数据 response.data: {total : 100, rows : [{},{}]}
            $scope.resultMap = response.data;
        });
    };
});
