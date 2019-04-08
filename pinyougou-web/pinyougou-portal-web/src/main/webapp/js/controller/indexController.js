/** 定义首页控制器层 */
app.controller("indexController", function($scope, baseService){

    // 查询首页广告数据
    $scope.findContentByCategoryId = function (categoryId) {
        baseService.sendGet("/findContentByCategoryId?categoryId="
            + categoryId).then(function(response){
            // 获取响应数据 List<Content> [{},{}]
            $scope.contentList = response.data;
        });
    };

});