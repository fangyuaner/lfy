package cn.itcast.core.service.cart;

import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;

import java.util.List;

public interface CartService {

    /**
     * 获取库存
     * @param id
     * @return
     */
    public Item findOne(Long id);

    /**
     * 重新进行填充页面需要展示的数据
     * @param cartList
     * @return
     */
    List<Cart> setAttributeForCart(List<Cart> cartList);

    /**
     * 将购物车保存到redis中
     * @param username
     * @param cartList
     */
    void mergeCartListToRedis(String username, List<Cart> cartList);

    /**
     * 从redis中获取购物车
     * @param username
     * @return
     */
    List<Cart> findCartListFromRedis(String username);
}
