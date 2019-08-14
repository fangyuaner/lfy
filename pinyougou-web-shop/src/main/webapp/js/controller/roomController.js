 //控制层 
app.controller('roomController' ,function($scope,$controller,roomService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //查询
	$scope.findAll=function(){
		roomService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		roomService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	
	$scope.add = function(){
		roomService.add( $scope.entity  ).success(
			function(response){
				if(response.flag){
					// 重新查询 
		        	// $scope.reloadList();//重新加载
					location.href="shoplogin.html";
				}else{
					alert(response.message);
				}
			}		
		);	
	}
	
	 
	//批量删除
	$scope.dele=function(){			
		//获取选中的复选框			
		roomService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}

});	
