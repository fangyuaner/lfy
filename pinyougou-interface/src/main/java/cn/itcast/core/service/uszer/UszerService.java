package cn.itcast.core.service.uszer;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.uszer.Uszer;

import java.util.List;

public interface UszerService {


     // 用户注册
    public void add(Uszer uszer);


    //回显用户信息!!!!z
    public Uszer findOne(Integer uszerId);


    public Uszer findOneByUsername(String username);


    //查询所有用户
    List<Uszer> findAll();

    //更新
    public void update(Uszer uszer);

    //分页查询
    PageResult findPage(Integer pageNo, Integer pageSize);
}
