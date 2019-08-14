//服务层
app.service('roomService',function($http){
	    	
	//查询
	this.findAll=function(){
		return $http.get('../room/findAll.do');		
	}
	//分页
	this.findPage=function(page,rows){
		return $http.get('../room/findPage.do?page='+page+'&rows='+rows);
	}

	//增加 
	this.add=function(entity){
		return  $http.post('../room/add.do',entity );
	}

	//删除
    this.dele = function(ids){
        return $http.get("../room/delete.do?ids="+ids);
    }
});
