package cn.itcast.core.controller.cart;

import cn.itcast.core.entity.Result;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.service.cart.CartService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference
    private CartService cartService;

    // 服务器端需要支持跨域访问
//            response.setHeader("Access-Control-Allow-Origin", "http://localhost:9003"); // 支持跨域
//            response.setHeader("Access-Control-Allow-Credentials", "true"); // 支持携带cookie信息

    /**
     * 商品加入购物车
     * @param itemId
     * @param num
     * @return
     */
    @RequestMapping("/addGoodsToCartList.do")
    // 默认的allowCredentials = "true"，无需设置
    @CrossOrigin(origins = {"http://localhost:9003"})
    public Result addGoodsToCartList(Long itemId, Integer num,
                                     HttpServletRequest request, HttpServletResponse response){
        try {
            // 商品加入购物车业务逻辑
            // 1、定义一个空车的集合
            List<Cart> cartList = null;
            // 实际的业务开发中：通过定义一个简单标识（开关），可以完成复杂业务
            boolean flag = false;   // 默认：关闭
            // 2、判断本地(cookie)是否有购物车
            Cookie[] cookies = request.getCookies();
            if(cookies != null && cookies.length > 0){
                for (Cookie cookie : cookies) { // 数据：key-value
                    if("BUYER_CART".equals(cookie.getName())){
                        // 3、有：直接取出来
                        String text = cookie.getValue();    // json串
                        cartList = JSON.parseArray(text, Cart.class);
                        flag = true;
                        break;  // 找到车子，跳出循环
                    }
                }
            }
            // 4、没有：创建一个车子
            if(cartList == null){
                cartList = new ArrayList<>();
            }
            // ===有车了
            // 创建Cart并且封装数据：重要的数据
            Item item = cartService.findOne(itemId);
            Cart cart = new Cart();
            cart.setSellerId(item.getSellerId()); // 商家id
//            cart.setSellerName(); // 商家名称
            List<OrderItem> orderItemList = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setItemId(itemId);
            orderItem.setNum(num);
            orderItemList.add(orderItem);
            cart.setOrderItemList(orderItemList);
            // 5、将商品进行装车
//            cartList.add(cart);
            // 5-1、判断该商品是否属于同一个商家(本质：判断sellerId)
            int sellerIndexOf = cartList.indexOf(cart);
            if(sellerIndexOf != -1){
                // 同一个商家
                // 继续判断：是否是同款商品(本质：判断itemId)
                List<OrderItem> oldOrderItemList = cartList.get(sellerIndexOf).getOrderItemList();
                int itemIndexOf = oldOrderItemList.indexOf(orderItem);
                if(itemIndexOf != -1){
                    // 属于同款商品：合并数量
                    OrderItem oldOrderItem = oldOrderItemList.get(itemIndexOf);
                    oldOrderItem.setNum(oldOrderItem.getNum() + num);
                }else{
                    // 同一个商家但不同商品
                    oldOrderItemList.add(orderItem);
                }
            }else{
                // 不是同一个商家，直接装车
                cartList.add(cart);
            }
            // 判断用户是否登录
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if(!"anonymousUser".equals(username)){
                // 6-1：已登录：将购物车保存到redis中 hash key filed（用户） value（购物车）
                cartService.mergeCartListToRedis(username, cartList);
                // 前提：本地有购物车，才清空本地的购物车
                if(flag){
                    Cookie cookie = new Cookie("BUYER_CART", null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/"); // cookie共享   9003/  9103/
                    response.addCookie(cookie);
                }
            }else{
                // 6-2、未登录：将购物车保存到本地(cookie)
                Cookie cookie = new Cookie("BUYER_CART", JSON.toJSONString(cartList));
                cookie.setMaxAge(60*60);
                cookie.setPath("/"); // cookie共享   9003/  9103/
                response.addCookie(cookie);
            }

            return new Result(true, "添加购物车成功");
        } catch (Exception e){
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }
    }

    /**
     * 回显购物车的列表数据
     * @return
     */
    @RequestMapping("/findCartList.do")
    public List<Cart> findCartList(HttpServletRequest request, HttpServletResponse response){
        // 未登录：从cookie中取出购物车
        List<Cart> cartList = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) { // 数据：key-value
                if("BUYER_CART".equals(cookie.getName())){
                    String text = cookie.getValue();    // json串
                    cartList = JSON.parseArray(text, Cart.class);
                    break;  // 找到车子，跳出循环
                }
            }
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!"anonymousUser".equals(username)){
            // 已登录：从redis中取出购物车
            // 场景：用户未登录状态下将商品加入了购物车
            // 用户又去登录并且成功后，点击页面中【我的购物车】----> 需要同步
            if(cartList != null){
                cartService.mergeCartListToRedis(username, cartList);
                // 清空
                Cookie cookie = new Cookie("BUYER_CART", null);
                cookie.setMaxAge(0);
                cookie.setPath("/"); // cookie共享   9003/  9103/
                response.addCookie(cookie);
            }
            // 需要：将本地的购物车同步到redis中
            cartList = cartService.findCartListFromRedis(username);
        }

        // 取出来的数据并不完整，因此需要重新进行填充页面需要展示的数据
        if(cartList != null){
            cartList = cartService.setAttributeForCart(cartList);
        }
        return cartList;
    }
}
