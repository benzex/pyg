/** 定义搜索控制器 */
app.controller("searchController" ,function ($scope, $sce, $location, baseService) {

    // 封装搜索参数的json对象
    $scope.searchParam = {keywords : '', category : '', brand : '',
        spec : {}, price : '', page : 1, rows : 10,
        sortField : '', sortValue : ''};

    // 搜索方法
    $scope.search = function () {
        // 发送异步请求
        baseService.sendPost("/Search", $scope.searchParam).then(function (response) {
            // 获取响应数据 response.data: {total : 100, rows : [{},{}]}
            $scope.resultMap = response.data;

            // 页面显示的关键字
            $scope.keyword = $scope.searchParam.keywords;

            // 调用生成页码的方法
            initPageNums();
        });
    };

    /** 生成页码的方法 */
    var initPageNums = function () {
        // pageNums
        $scope.pageNums = [];
        // 开始页码
        var firstPage = 1;
        // 结束页码
        var lastPage = $scope.resultMap.totalPages;

        // 前面加点
        $scope.firstDot = true;
        // 后面加点
        $scope.lastDot = true;

        // 判断总页数是不是大于5
        if ($scope.resultMap.totalPages > 5){

            // 判断当前页码是否靠前面近些
            if ($scope.searchParam.page <= 3){ // 前面
                lastPage = 5;
                $scope.firstDot = false;
            }else if($scope.searchParam.page >= $scope.resultMap.totalPages - 3){ // 后面
                // 判断当前页码是否靠后面近些
                firstPage = $scope.resultMap.totalPages - 4;
                $scope.lastDot = false;
            }else{ // 中间
                firstPage = $scope.searchParam.page - 2;
                lastPage = $scope.searchParam.page + 2;
            }
        }else {
            $scope.firstDot = false;
            $scope.lastDot = false;
        }

        // 循环生成5个页码
        for (var i = firstPage; i <= lastPage; i++){
            $scope.pageNums.push(i);
        }
    };

    // 把html格式字的字符串转化成html标签
    $scope.trustHtml = function (html) {
        return $sce.trustAsHtml(html);
    };

    // 添加过滤条件
    $scope.addSearchItem = function (key, value) {
        // 判断key 是 商品分类、品牌、价格
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchParam[key] = value;
        }else{ // 规格
            $scope.searchParam.spec[key] = value;
        }
        // 执行搜索
        $scope.search();
    };

    // 删除过滤条件
    $scope.removeSearchItem = function (key) {
        // 判断key 是 商品分类、品牌、价格
        if (key == 'category' || key == 'brand' || key == 'price'){
            $scope.searchParam[key] = "";
        }else{ // 规格
            // delete删除json对象中的key value对
            delete $scope.searchParam.spec[key];
        }
        // 执行搜索
        $scope.search();
    };


    // 根据页码查询
    $scope.pageSearch = function (page) {
        page = parseInt(page);
        // 判断页码的有效性: 不能小于1、不能大于总页数、不能等于当前页码
        if (page >= 1 && page <= $scope.resultMap.totalPages
            && page != $scope.searchParam.page){
            // 当前页码
            $scope.searchParam.page = page;
            // 跳转的页码
            $scope.jumpPage = page;
            // 执行搜索
            $scope.search();
        }
    };

    // 排序查询
    $scope.sortSearch = function (field, value) {
        $scope.searchParam.sortField = field;
        $scope.searchParam.sortValue = value;
        // 当前页码
        $scope.searchParam.page = 1;
        // 执行搜索
        $scope.search();
    };

    // 获取首页传过的来的关键字
    $scope.getKeywords = function () {

        // http://search.pinyougou.com/?keywords=小米
        // 获取请求URL后面的参数 {}
        var obj = $location.search();
        //alert(JSON.stringify(obj));

        $scope.searchParam.keywords = obj.keywords;
        // 执行搜索
        $scope.search();
    };


});
