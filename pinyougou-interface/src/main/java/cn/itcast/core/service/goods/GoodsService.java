package cn.itcast.core.service.goods;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.vo.GoodsVo;

public interface GoodsService {

    /**
     * 录入商品
     * @param goodsVo
     */
    public void add(GoodsVo goodsVo);

    /**
     * 查询当前商家下的商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    PageResult search(Integer page, Integer rows, Goods goods);

    /**
     * 回显商品
     * @param id
     * @return
     */
    public GoodsVo findOne(Long id);

    /**
     * 更新商品
     * @param goodsVo
     */
    public void update(GoodsVo goodsVo);

    /**
     * 商家系统下的商品的列表查询
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    public PageResult searchForManager(Integer page, Integer rows, Goods goods);

    /**
     * 商品审核
     * @param ids
     * @param auditStatus
     */
    public void updateStatus(Long[] ids, String auditStatus);

    /**
     * 商品的删除
     * @param ids
     */
    public void delete(Long[] ids);
}
