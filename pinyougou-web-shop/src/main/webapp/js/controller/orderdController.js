 //控制层 
app.controller('orderdController' ,function($scope,$controller,orderdService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //查询
	$scope.findAll=function(){
		orderdService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		orderdService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

	//新增
	$scope.add = function(){
		orderdService.add( $scope.entity  ).success(
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
		orderdService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}


    // 保存修改订单的方法:
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.entity.id != null){
            // 更新
            object = orderdService.update($scope.entity);
        }else{
            // 保存
            object = orderdService.add($scope.entity);
        }
        object.success(function(response){
            // {flag:true,message:xxx}
            // 判断保存是否成功:
            if(response.flag){
                // 保存成功
                alert(response.message);
                $scope.reloadList();
            }else{
                // 保存失败
                alert(response.message);
            }
        });
    }

});	
