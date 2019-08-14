package cn.itcast.core.dao.orderd;

import cn.itcast.core.pojo.orderd.Orderd;
import cn.itcast.core.pojo.orderd.OrderdQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderdDao {
    int countByExample(OrderdQuery example);

    int deleteByExample(OrderdQuery example);

    int deleteByPrimaryKey(Long orderdId);

    int insert(Orderd record);

    int insertSelective(Orderd record);

    List<Orderd> selectByExample(OrderdQuery example);

    Orderd selectByPrimaryKey(Long orderdId);

    int updateByExampleSelective(@Param("record") Orderd record, @Param("example") OrderdQuery example);

    int updateByExample(@Param("record") Orderd record, @Param("example") OrderdQuery example);

    int updateByPrimaryKeySelective(Orderd record);

    int updateByPrimaryKey(Orderd record);

    void deleteByPrimaryKeys(Long[] ids);
}