package cn.itcast.core.service.orderd;

import cn.itcast.core.pojo.orderd.Orderd;

import java.util.List;

public interface OrderdService {
    //查询所有order
    List<Orderd> findAll();

    //新增order
    void add(Orderd orderd);

    //取消（删除）order
    public void delete(Long[] ids);

    //修改order

    void update(Orderd orderd);

}
