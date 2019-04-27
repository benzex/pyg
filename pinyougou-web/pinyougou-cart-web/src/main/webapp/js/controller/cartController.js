// 定义购物车的控制器
app.controller('cartController', function ($scope, $controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});


    // 添加商品到购物车
    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新查询购物车
                $scope.findCart();
            }
        });
    };

    // 查找购物车
    $scope.findCart=function (){
        $scope.totalNum = 0;
        $scope.portArray = {};
        $scope.chooseArray={};
        $scope.num = 0;
        baseService.sendGet("/cart/findCart").then(
            function (response) {
                $scope.carts = response.data;
                for(var i = 0;i < $scope.carts.length; i++){
                    var cart = $scope.carts[i];
                    $scope.chooseArray[cart.sellerId]=new Array();
                    $scope.portArray[cart.sellerId] = false;
                    for (var j = 0; j < cart.orderItems.length; j++){
                        $scope.num += 1;
                        $scope.chooseArray[cart.sellerId][j]=false;
                        $scope.totalNum += cart.orderItems[j].num;
                    }
                }
            }
        )
    };


    //封装条件
    $scope.ids=[];
    //单选控制
    $scope.choose = function ($event, i,id, money, seller) {
        $scope.chooseArray[seller][i] = $event.target.checked;
        if ($event.target.checked) {
            $scope.ids.push(id);
        }
        else {
            var number = $scope.ids.indexOf(id);
            $scope.ids.splice(number, 1);
        }
        $scope.portArray[seller] = $scope.$isSelect(seller);
        $scope.clkd=$scope.num==$scope.ids.length;
    };
    // 判断是否商家全部元素是否选中
    $scope.$isSelect= function(seller){
        for (var a = 0; a < $scope.chooseArray[seller].length ;a++ ) {
            if ($scope.chooseArray[seller][a] == false){
                return false;
            }
        }
        return true;
    };
    //商家选择控制
    $scope.portChoose = function($event,id,i,cart){
        $scope.chooseArray[cart.sellerId]=new Array();
        $scope.portArray[cart.sellerId] = $event.target.checked;
        for (var j = 0; j < cart.orderItems.length; j++) {
            $scope.chooseArray[cart.sellerId][j] = $event.target.checked;
            if ($event.target.checked) {
                if ($scope.ids.indexOf(cart.orderItems[j].itemId)== -1) {
                    $scope.ids.push(cart.orderItems[j].itemId);
                    $scope.chooseArray[j] = $event.target.checked;
                }
            }else {
                var number = $scope.ids.indexOf(cart.orderItems[j].itemId);
                $scope.ids.splice(number, 1);
            }
        }
        $scope.clkd=$scope.num==$scope.ids.length;
    };
    //全选控制
    $scope.allChoose = function ($event) {
        $scope.ids=[];
        $scope.totalEntity = {totalNum :0, totalMoney :0.00};
        $scope.portArray = {};
        $scope.chooseArray={};
        for(var i=0;i<$scope.carts.length;i++) {
            var cart = $scope.carts[i];
            $scope.portArray[cart.sellerId] = $event.target.checked;
            var s = $event.target.checked;
            $scope.chooseArray[cart.sellerId]=new Array();
            for (var j = 0; j < cart.orderItems.length; j++) {
                $scope.chooseArray[cart.sellerId][j]=s;
                if (s){
                    $scope.ids.push(cart.orderItems[j].itemId);
                }
            }
        }
        $scope.clkd=$scope.num==$scope.ids.length;
    };
    $scope.totalEntity = {totalNum :0, totalMoney :0.00};
    //调控总价格及数量
    $scope.$watch('ids.length',function (newVal,oldVal) {
        if (newVal != oldVal){
            $scope.totalEntity = {totalNum :0, totalMoney :0.00};
            for(var i = 0;i < $scope.carts.length; i++){
                var cart = $scope.carts[i];
                for (var j = 0; j < cart.orderItems.length; j++){
                    var orderItems = cart.orderItems[j];
                    if ($scope.ids.indexOf(orderItems.itemId)!= -1) {
                        $scope.totalEntity.totalNum += orderItems.num;
                        $scope.totalEntity.totalMoney += orderItems.totalFee;
                    }
                }
            }
        }
    });
});
