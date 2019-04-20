/** 定义首页控制器层 */
app.controller("indexController", function($scope, $controller, baseService){


    // 继承baseController
    $controller('baseController', {$scope : $scope});


    // 查询首页广告数据
    $scope.findContentByCategoryId = function (categoryId) {
        baseService.sendGet("/findContentByCategoryId?categoryId="
            + categoryId).then(function(response){
            // 获取响应数据 List<Content> [{},{}]
            $scope.contentList = response.data;
        });
    };

    // 跳转到搜索系统
    $scope.search = function () {
        var keyword = $scope.keywords ?  $scope.keywords : "";
        location.href = "http://search.pinyougou.com?keywords=" + keyword;
    };

});