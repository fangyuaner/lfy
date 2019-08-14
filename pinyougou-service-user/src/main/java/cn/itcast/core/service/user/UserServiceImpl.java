package cn.itcast.core.service.user;

import cn.itcast.core.dao.user.UserDao;
import cn.itcast.core.pojo.user.User;
import cn.itcast.core.utils.md5.MD5Util;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination smsDestination;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserDao userDao;

    /**
     * 获取手机短信验证码
     * @param phone
     */
    @Override
    public void sendCode(final String phone) {
        // 生成短信的验证码：6位
        final String code = RandomStringUtils.randomNumeric(6);
        System.out.println("code:"+code);
        // 短信验证码保存
        redisTemplate.boundValueOps(phone).set(code);
        // 短信验证码的过期时间 查看key过期时间 ttl
        redisTemplate.boundValueOps(phone).expire(5, TimeUnit.MINUTES);
        // 将数据封装到map中并且发送到mq中
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // 创建map消息体
                MapMessage mapMessage = session.createMapMessage();
                // 封装数据
                mapMessage.setString("phoneNumbers", phone);
                mapMessage.setString("signName", "阮文");
                mapMessage.setString("templateCode", "SMS_140720901");
                mapMessage.setString("templateParam", "{\"code\":\""+code+"\"}");
                return mapMessage;
            }
        });
    }

    /**
     * 用户注册
     * @param user
     * @param smscode
     */
    @Transactional
    @Override
    public void add(User user, String smscode) {
        // 判断验证码是否正确
        String code = (String) redisTemplate.boundValueOps(user.getPhone()).get();
        if(smscode != null && !"".equals(smscode) && code.equals(smscode)){
            // 验证码正确
            // 密码加密MD5
            String password = MD5Util.MD5Encode(user.getPassword(), null);
            user.setPassword(password);
            user.setCreated(new Date());
            user.setUpdated(new Date());
            userDao.insertSelective(user);
        }else{
            throw new RuntimeException("验证码不正确");
        }
    }
}
