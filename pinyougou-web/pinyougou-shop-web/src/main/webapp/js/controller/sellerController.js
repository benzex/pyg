/** 定义控制器层 */
app.controller('sellerController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /** 添加或修改 */
    $scope.saveOrUpdate = function(){

        /** 发送post请求 */
        baseService.sendPost("/seller/save", $scope.seller)
            .then(function(response){
                if (response.data){
                    /** 跳转到登录页面 */
                    location.href = "/shoplogin.html";
                }else{
                    alert("操作失败1！");
                }
            });
    };

    /** 添加或修改 */
    $scope.update = function(){

        /** 发送post请求 */
        baseService.sendPost("/seller/update", $scope.seller)
            .then(function(response){
                if (response.data){
                    /** 跳转到登录页面 */
                    //location.href = "/shoplogin.html";
                }else{
                    alert("操作失败！");
                }
            });
    };
    /** 添加或修改 */
    $scope.updateAndCheck= function(){
        $scope.seller
        /** 发送post请求 */
        baseService.sendPost("/seller/checkps", $scope.seller)
            .then(function(response){
                if (response.data){
                    /** 跳转到登录页面 */
                    $scope.updatePassword();
                    //location.href = "/shoplogin.html";
                }else{
                    alert("操作失败！"+response.data);
                }
            });
    };
  //  $scope.newPassword="";
    /** 添加或修改 */



    $scope.updatePassword = function(){
        /** 发送post请求 */
        alert($scope.seller.password);
        $scope.seller.password=$scope.newPassword;
        alert($scope.seller.password);

        baseService.sendPost("/seller/update", $scope.seller)
            .then(function(response){
                if (response.data){

                    /** 跳转到登录页面 */
                    //location.href = "/shoplogin.html";
                }else{
                    alert("操作失败2！");
                }
            });
    };



    // /** 修改密码 */
    // $scope.newPs="";
    // /** 分页查询(查询条件) */
    // $scope.updatePs = function(){
    //     alert( $scope.searchEntity)
    //     baseService.sendPost("/seller/updatePs", $scope.newPs)
    //         .then(function(response){
    //             if (response.data){
    //                 /** 跳转到登录页面 */
    //                 //location.href = "/shoplogin.html";
    //             }else{
    //                 alert("操作失败！");
    //             }
    //         });
    // };


    /** 查询条件对象 */
    $scope.searchEntity = {};
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

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };
});