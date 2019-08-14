package cn.itcast.core.listener;

import cn.itcast.core.service.search.ItemSearchService;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 自定义消息监听器：将商品下架
 */
public class ItemDeleteListener implements MessageListener {

    @Resource
    private ItemSearchService itemSearchService;

    /**
     * 获取消息-消费消息
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            // 获取消息
            ActiveMQTextMessage activeMQTextMessage = (ActiveMQTextMessage) message;
            String id = activeMQTextMessage.getText();
            System.out.println("删除商品的id：" + id);
            // 消费消息
            itemSearchService.deleteItemFromSolr(Long.parseLong(id));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
