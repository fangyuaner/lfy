package cn.itcast.core.service.address;

import cn.itcast.core.dao.address.AddressDao;
import cn.itcast.core.pojo.address.Address;
import cn.itcast.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private AddressDao addressDao;

    /**
     * 获取当前登录人的地址列表 tom tomcat
     * @param userId
     * @return
     */
    @Override
    public List<Address> findListByLoginUser(String userId) {
        AddressQuery query = new AddressQuery();
        query.createCriteria().andUserIdEqualTo(userId);
        return addressDao.selectByExample(query);
    }
}
