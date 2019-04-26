/** 定义控制器层 */
app.controller('sellerController', function ($scope, $controller, baseService) {

    /** 指定继承baseController */
    $controller('baseController', {$scope: $scope});

    /** 添加 */
    $scope.saveOrUpdate = function () {

        /** 发送post请求 */
        baseService.sendPost("/seller/save", $scope.seller)
            .then(function (response) {
                if (response.data) {
                    /** 跳转到登录页面 */
                    location.href = "/shoplogin.html";
                } else {
                    alert("操作失败1！");
                }
            });
    };


    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function (page, rows) {
        baseService.findByPage("/seller/findByPage", page,
            rows, $scope.searchEntity)
            .then(function (response) {
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 显示修改 */
    $scope.show = function (entity) {
        /** 把json对象转化成一个新的json对象 */
        $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function () {
        if ($scope.ids.length > 0) {
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function (response) {
                    if (response.data) {
                        /** 重新加载数据 */
                        $scope.reload();
                    } else {
                        alert("删除失败！");
                    }
                });
        } else {
            alert("请选择要删除的记录！");
        }
    };

    $scope.checkOldNew = function () {
        if ($scope.seller.password == $scope.newPassword1) {
            $scope.warning1 = "输入的新旧密码不能一样";
        } else {
            $scope.warning1 = "";
        }
    };
    $scope.checkNew = function () {
        if ($scope.newPassword1 != $scope.newPassword2) {
            $scope.warning2 = "输入的新密码不一样";
        } else {
            $scope.warning2 = "";
        }
    };
    $scope.updatePW = function () {
        if ($scope.newPassword1 == null || $scope.newPassword2 == null || $scope.seller.password == null) {
            $scope.warning1 = "密码不能为空";
        }
        else if ($scope.seller.password == $scope.newPassword1) {
            $scope.warning1 = "新旧密码不能一样";
        } else if ($scope.newPassword1 != $scope.newPassword2) {
            $scope.warning2 = "新密码不一样";
        } else if ($scope.seller.password != $scope.newPassword1
            && $scope.newPassword1 == $scope.newPassword2) {
            $scope.updateAndCheck()
        }
    }
    /** 修改资料 */
    $scope.update = function () {

        /** 发送post请求 */
        baseService.sendPost("/seller/update", $scope.seller)
            .then(function (response) {
                if (response.data) {
                    /** 跳转到登录页面 */
                    alert("恭喜你,资料修改成功!");
                    //location.href = "/shoplogin.html";
                } else {
                    alert("操作失败！");
                }
            });
    };
    /** 验证原密码和修改密码 */
    $scope.updateAndCheck = function () {
        $scope.seller

        baseService.sendPost("/seller/checkps", $scope.seller)
            .then(function (response) {
                if (response.data) {
                    /** 跳转到登录页面 */
                    $scope.updatePassword();
                } else {
                    alert("原密码输入错误！" + response.data);
                }
            });
    };
    /**修改密码的方法*/
    $scope.updatePassword = function () {
        /** 发送post请求 */

        alert($scope.seller.password);
        $scope.seller.password = $scope.newPassword1;
        alert($scope.seller.password);
        baseService.sendPost("/seller/update", $scope.seller)
            .then(function (response) {
                if (response.data) {
                    alert("恭喜你,密码修改成功!马上进入登录页面...");
                    /** 跳转到登录页面 */
                    location.href = "/logout";
                } else {
                    alert("操作失败！");
                }
            });

    };
});
