package cn.itcast.core.dao.uszer;

import cn.itcast.core.pojo.uszer.Uszer;
import cn.itcast.core.pojo.uszer.UszerQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UszerDao {
    int countByExample(UszerQuery example);

    int deleteByExample(UszerQuery example);

    int deleteByPrimaryKey(Integer id);

    int insert(Uszer record);

    int insertSelective(Uszer record);

    List<Uszer> selectByExample(UszerQuery example);

    Uszer selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Uszer record, @Param("example") UszerQuery example);

    int updateByExample(@Param("record") Uszer record, @Param("example") UszerQuery example);

    int updateByPrimaryKeySelective(Uszer record);

    int updateByPrimaryKey(Uszer record);

    Uszer selectByUsername(String username);
}