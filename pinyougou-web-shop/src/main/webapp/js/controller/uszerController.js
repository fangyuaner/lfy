 //控制层 
app.controller('uszerController' ,function($scope,$controller,uszerService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		uszerService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		uszerService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	

    // 查询一个:
    $scope.findById = function(id){
        uszerService.findOne(id).success(function(response){
            // {id:xx,name:yy,firstChar:zz}
            $scope.entity = response;
        });
    }


    // 保存修改用户信息的方法:
    $scope.save = function(){
        // 区分是保存还是修改
        var object;
        if($scope.entity.id != null){
            // 更新
            object = uszerService.update($scope.entity);
        }else{
            // 保存
            object = uszerService.add($scope.entity);
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


	//注册用户
	$scope.add = function(){
		uszerService.add( $scope.entity  ).success(
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
	

});	
