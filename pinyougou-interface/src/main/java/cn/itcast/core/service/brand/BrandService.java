package cn.itcast.core.service.brand;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    /**
     * 查询所有品牌
     * @return
     */
    public List<Brand> findAll();

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    public PageResult findPage(Integer pageNo, Integer pageSize);

    /**
     * 条件查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand);

    /**
     * 保存品牌
     * @param brand
     */
    public void add(Brand brand);

    /**
     * 回显品牌
     * @param id
     * @return
     */
    public Brand findOne(Long id);

    /**
     * 更新品牌
     * @param brand
     */
    public void update(Brand brand);

    /**
     * 批量删除
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 新增模板时加载品牌结果集
     * @return
     */
    List<Map<String,String>> selectOptionList();
}
