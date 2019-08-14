package cn.itcast.core.service.search;

import java.util.Map;

public interface ItemSearchService {

    /**
     * 前台系统检索
     * @param searchMap
     * @return
     */
    public Map<String, Object> search(Map<String, String> searchMap);

    /**
     * 商品上架---保存到索引库
     * @param id
     */
    public void isShow(Long id);

    /**
     * 商品下架---从索引库删除商品
     * @param id
     */
    public void deleteItemFromSolr(Long id);
}
