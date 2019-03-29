// 品牌控制器层
app.controller('brandController', function ($scope, $controller, baseService) {

    // 品牌控制器继承基础控制器
    // 第一个$scope为 baseController
    // 第二个$scope为 brandController
    $controller('baseController', {$scope:$scope});

    // 分页查询品牌
    $scope.search = function (page, rows) {
        // 发送异步请求
        baseService.findByPage("/brand/findByPage", page, rows, $scope.searchEntity)
            .then(function(response){
            // 获取响应数据
            // response.data: {total: 1000, rows : [{},{}]}
            // 获取品牌数据
            $scope.dataList = response.data.rows;
            // 获取总记录数
            $scope.paginationConf.totalItems = response.data.total;

        });
    };

    // 添加或修改品牌
    $scope.saveOrUpdate = function () {
        // 请求参数
        //alert(JSON.stringify($scope.entity));
        var url = "save"; // 添加URL
        // 判断id是否存在
        if ($scope.entity.id){
            url = "update"; // 修改URL
        }
        // 发送异步请求
        baseService.sendPost("/brand/" + url, $scope.entity).then(function(response){
            // 获取响应数据 true|false
            if (response.data){
                // 重新查询品牌数据
                $scope.reload();
            }else{
                alert("操作失败！");
            }
        });
    };


    // 修改按钮点击事件
    $scope.show = function (entity) {
        // 把entity转化成json字符串
        var jsonStr = JSON.stringify(entity);
        // 把json字符串转化成json对象
        $scope.entity = JSON.parse(jsonStr);
    };


    // 为删除按钮绑定点击事件
    $scope.delete = function () {
        if ($scope.ids.length > 0){
            if (confirm("您确定要删除吗?")){
                // 发送异步请求
                baseService.deleteById("/brand/delete", $scope.ids)
                    .then(function(response){
                    // 获取响应数据 true|false
                    if (response.data){
                        // 重新加载数据
                        $scope.reload();
                        // 清空ids数组
                        $scope.ids = [];
                    }else {
                        alert("删除失败！");
                    }
                });
            }
        }else{
            alert("请选择要删除的品牌！");
        }
    };

});