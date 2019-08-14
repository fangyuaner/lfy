package cn.itcast.core.service.order;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.order.OrderDao;
import cn.itcast.core.dao.order.OrderItemDao;
import cn.itcast.core.pojo.cart.Cart;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.pojo.order.OrderItem;
import cn.itcast.core.utils.uniquekey.IdWorker;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private OrderItemDao orderItemDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IdWorker idWorker;

    @Resource
    private ItemDao itemDao;

    /**
     * 提交订单的操作
     * @param username
     * @param order
     */
    @Override
    public void add(String username, Order order) {
        // 订单已商家为单位
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("BUYER_CART").get(username);
        if(cartList != null && cartList.size() > 0){
            for (Cart cart : cartList) {
                // 保存订单
                long orderId = idWorker.nextId();
                order.setOrderId(orderId);  // 订单主键
                double payment = 0f;        // 当前商家下所有的商品金额
                order.setPaymentType("1");  // 支付类型：在线支付
                order.setStatus("1");       // 订单状态：待付款
                order.setCreateTime(new Date());
                order.setUpdateTime(new Date());
                order.setUserId(username);  // 下单的用户
                order.setSourceType("2");   // 订单来源：pc端
                order.setSellerId(cart.getSellerId());  // 商家id

                // 保存订单明细：购物项
                List<OrderItem> orderItemList = cart.getOrderItemList();
                if(orderItemList != null && orderItemList.size() > 0){
                    for (OrderItem orderItem : orderItemList) {
                        Item item = itemDao.selectByPrimaryKey(orderItem.getItemId());
                        long id = idWorker.nextId();
                        orderItem.setId(id);    // 主键
                        orderItem.setGoodsId(item.getGoodsId());    // 商品id
                        orderItem.setOrderId(orderId);  // 订单id
                        orderItem.setTitle(item.getTitle());    // 标题
                        orderItem.setPrice(item.getPrice());    // 单价
                        double totalFee = item.getPrice().doubleValue() * orderItem.getNum();
                        payment += totalFee;
                        orderItem.setTotalFee(new BigDecimal(totalFee));
                        orderItem.setPicPath(item.getImage());
                        orderItem.setSellerId(item.getSellerId());
                        // 明细
                        orderItemDao.insertSelective(orderItem);
                    }
                }
                order.setPayment(new BigDecimal(payment));
                // 保存订单
                orderDao.insertSelective(order);
            }
        }
        // 删除购物车
        redisTemplate.boundHashOps("BUYER_CART").delete(username);

    }
}
