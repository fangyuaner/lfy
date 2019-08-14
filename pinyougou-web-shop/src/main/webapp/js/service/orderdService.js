//服务层
app.service('orderdService',function($http){
	    	
	//查询
	this.findAll=function(){
		return $http.get('../orderd/findAll.do');		
	}
	//分页
	this.findPage=function(page,rows){
		return $http.get('../orderd/findPage.do?page='+page+'&rows='+rows);
	}

	//增加 
	this.add=function(entity){
		return  $http.post('../orderd/add.do',entity );
	}

	//删除
    this.dele = function(ids){
        return $http.get("../orderd/delete.do?ids="+ids);
    }

    //修改 
    this.update=function(entity){
        return  $http.post('../orderd/update.do',entity );
    }
});
