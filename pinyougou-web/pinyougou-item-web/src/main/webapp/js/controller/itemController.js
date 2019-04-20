// 商品详情控制器
app.controller('itemController', function ($scope, $http) {

    // 购买数量加减操作
    $scope.addNum = function (x) {
        $scope.num = parseInt($scope.num);
        $scope.num += x;
        if ($scope.num < 1){
            $scope.num = 1;
        }
    };

    // 定义变量记录用户选中的规格选项
    $scope.spec = {};

    // 记录用户选中的规格选项
    $scope.selectSpec = function (specName, optionName) {
        $scope.spec[specName] = optionName;

        // 根据用户选中的规格选项从SKU数组中找到对应的SKU
        $scope.searchSku();
    };

    // 判断是否为用户选中的规格选项
    $scope.isSelected = function (specName, optionName) {
        return $scope.spec[specName] == optionName;
    };

    // 加载默认的SKU
    $scope.loadSku = function () {
        // 从SKU商品的数组中取第一个SKU商品
        $scope.sku = skuList[0];
        // 默认的SKU的规格选项
        $scope.spec = JSON.parse($scope.sku.spec);
    };

    // 根据用户选中的规格选项从SKU数组中找到对应的SKU
    $scope.searchSku = function () {
        // 迭代SKU数组
        for (var i = 0; i < skuList.length; i++){
            var sku = skuList[i];
            if (sku.spec == JSON.stringify($scope.spec)){
                $scope.sku = sku;
                break;
            }
        }
    };

    // 为加入购物车按钮绑定点击事件
    $scope.addToCart = function () {
        // 发送跨域的异步请求
        // http://item.pinyougou.com ---> http://cart.pinyougou.com
        $http.get("http://cart.pinyougou.com/cart/addCart?itemId="
            + $scope.sku.id + "&num=" + $scope.num, {withCredentials: true})
            .then(function(response){
                if (response.data){
                    // 跳转到购物车系统
                    location.href = "http://cart.pinyougou.com";
                }else{
                    alert("加入购物车失败！");
                }
        });
    };

});