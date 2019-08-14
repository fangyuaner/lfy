//服务层
app.service('uszerService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../uszer/findAll.do');		
	}
	//分页
	this.findPage=function(page,rows){
		return $http.get('../uszer/findPage.do?page='+page+'&rows='+rows);
	}
	//增加 
	this.add=function(entity){
		return  $http.post('../uszer/add.do',entity );
	}
	//修改 
	this.update=function(entity){
		return  $http.post('../uszer/update.do',entity );
	}

});
