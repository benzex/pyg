/** 定义控制器层 */
app.controller('sellerController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 查询条件对象 */
    $scope.searchEntity = {status : '0'};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/seller/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };
    /** 显示修改 */
    $scope.show = function(entity){
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 商家审核 */
    $scope.updateStatus = function (sellerId, status) {
        // 发送异步请求
        baseService.sendGet("/seller/updateStatus?sellerId="
            + sellerId + "&status=" + status).then(function(response){
                // 获取响应数据
                if (response.data){
                    // 重新加载数据
                    $scope.reload();
                }else{
                    alert("审核失败！");
                }
        });
    };


    /** 添加或修改 */
    $scope.saveOrUpdate = function(){
        var url = "save";
        if ($scope.entity.id){
            url = "update";
        }
        /** 发送post请求 */
        baseService.sendPost("/seller/" + url, $scope.entity)
            .then(function(response){
                if (response.data){
                    /** 重新加载数据 */
                    $scope.reload();
                }else{
                    alert("操作失败！");
                }
            });
    };



    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                        $scope.ids = [];
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };


    //反选
    $scope.selectInverse = function (event) {
       /* var idsTemp = [];
        angular.forEach($scope.dataList,function (data) {
               if(data.sellerId.checked ){
                   data.sellerId.checked = false;
               }else {
                   data.sellerId.checked = true;
                   idsTemp.push(data.sellerId);
               }
               $scope.ids = idsTemp;
        });*/

    }



});