package cn.itcast.core.dao.specification;

import cn.itcast.core.pojo.specification.Specification;
import cn.itcast.core.pojo.specification.SpecificationQuery;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SpecificationDao {
    int countByExample(SpecificationQuery example);

    int deleteByExample(SpecificationQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Specification record);

    int insertSelective(Specification record);

    List<Specification> selectByExample(SpecificationQuery example);

    Specification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Specification record, @Param("example") SpecificationQuery example);

    int updateByExample(@Param("record") Specification record, @Param("example") SpecificationQuery example);

    int updateByPrimaryKeySelective(Specification record);

    int updateByPrimaryKey(Specification record);

    // 新增模板时初始化规格结果集
    List<Map<String,String>> selectOptionList();
}