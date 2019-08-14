package cn.itcast.core.service.orderd;


import cn.itcast.core.dao.orderd.OrderdDao;
import cn.itcast.core.pojo.orderd.Orderd;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderdServiceImpl implements OrderdService {
    @Resource
    private OrderdDao orderdDao;

    //查询
    @Override
    public List<Orderd> findAll() {
        List<Orderd> orders = orderdDao.selectByExample(null);
        return orders;
    }
    //新增
    @Transactional
    @Override
    public void add(Orderd orderd) {
        orderdDao.insertSelective(orderd);
    }
    @Transactional
    @Override
    public void delete(Long[] ids) {
        orderdDao.deleteByPrimaryKeys(ids);
    }
    @Transactional
    @Override
    public void update(Orderd orderd) {
        orderdDao.updateByPrimaryKeySelective(orderd);
    }
}
