app.controller("baseController",function($scope){
	// 分页的配置的信息
	$scope.paginationConf = {
		 currentPage: 1, // 当前页数
		 totalItems: 0, // 总记录数
		 itemsPerPage: 5, // 每页显示多少条记录
		 perPageOptions: [5,10, 20, 30, 40, 50],// 显示多少条下拉列表
		 onChange: function(){ // 当页码、每页显示多少条下拉列表发生变化的时候，自动触发了
			$scope.reloadList();// 重新加载列表
		 }
	}; 
	
	$scope.reloadList = function(){
		// $scope.findByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
		$scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
	}
	
	// 定义一个数组:
	$scope.selectIds = [];
	// 更新复选框：
	$scope.updateSelection = function($event,id){
		// 复选框选中
		if($event.target.checked){
			// 向数组中添加元素
			$scope.selectIds.push(id);
		}else{
			// 从数组中移除
			var idx = $scope.selectIds.indexOf(id);
			$scope.selectIds.splice(idx,1);
		}
		
	}
	
	// 定义方法：获取JSON字符串中的某个key对应值的集合
	$scope.jsonToString = function(jsonStr,key){
		// arg0:json串，例如"[{"id":42,"text":"家具颜色"},{"id":43,"text":"家具规格"}]"
		// arg1:text
		// 将字符串转成JSOn:
		var jsonObj = JSON.parse(jsonStr);
		
		var value = "";
		for(var i=0;i<jsonObj.length;i++){
			
			if(i>0){
				value += ",";
			}
			
			value += jsonObj[i][key];
		}
		return value;
	}
	
});