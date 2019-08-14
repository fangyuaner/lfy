package cn.itcast.core.service.itemcat;

import cn.itcast.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {

    /**
     * 商品的分类的列表查询
     * @param parentId
     * @return
     */
    public List<ItemCat> findByParentId(Long parentId);

    /**
     * 新增商品：加载模板id
     * @param id
     * @return
     */
    public ItemCat findOne(Long id);

    /**
     * 商品列表：回显分类名称
     * @return
     */
    public List<ItemCat> findAll();

}
