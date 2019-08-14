package cn.itcast.core.service.goods;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.good.GoodsQuery;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import cn.itcast.core.service.staticpage.StaticPageService;
import cn.itcast.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private BrandDao brandDao;

    @Resource
    private SellerDao sellerDao;

    @Resource
    private SolrTemplate solrTemplate;

//    @Resource
//    private StaticPageService staticPageService;

    @Resource
    private JmsTemplate jmsTemplate;

    @Resource
    private Destination topicPageAndSolrDestination;

    @Resource
    private Destination queueSolrDeleteDestination;


    /**
     * 录入商品
     * @param goodsVo
     */
    @Transactional
    @Override
    public void add(GoodsVo goodsVo) {
        // 保存商品基本信息
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");  // 该商品待审核的状态
//        goods.setSellerId();    // 属于哪个商家
        goodsDao.insertSelective(goods); // 返回自增主键的id
        // 保存商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);
        // 保存商品对应的库存信息
        // 并不是所有的商品都有规格的
        if("1".equals(goods.getIsEnableSpec())){    // 启用规格：一个商品对应多个库存
            // 商品对应的库存
            List<Item> itemList = goodsVo.getItemList();
            if(itemList != null && itemList.size() > 0){
                for (Item item : itemList) {
                    // 设置库存的标题=spu名称+spu副标题+规格名称
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    // 规格名称：
                    // 栗子：{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);
                    setAttributeForItem(goods, goodsDesc, item);
                    // 保存库存
                    itemDao.insertSelective(item);
                }
            }
        }else{  // 不启用规格：一个商品对应一个库存
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());
            item.setIsDefault("1"); // 默认的库存信息
            item.setSpec("{}");     // 无规格
            item.setPrice(goods.getPrice());
            item.setNum(9999);      // 库存量
            setAttributeForItem(goods, goodsDesc, item);
            // 保存库存
            itemDao.insertSelective(item);
        }
    }

    /**
     * 查询当前商家下的商品列表
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {
        // 1、设置分页
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        GoodsQuery goodsQuery = new GoodsQuery();
        if(goods.getSellerId() != null){
            goodsQuery.createCriteria().andSellerIdEqualTo(goods.getSellerId());
        }
        // 3、根据条件查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        // 4、将结果封装并返回
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 回显商品
     * @param id
     * @return
     */
    @Override
    public GoodsVo findOne(Long id) {
        // 商品基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        // 商品描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        // 商品库存信息
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        // 将数据封装到vo中
        GoodsVo goodsVo = new GoodsVo();
        goodsVo.setGoods(goods);
        goodsVo.setGoodsDesc(goodsDesc);
        goodsVo.setItemList(itemList);
        return goodsVo;
    }

    /**
     * 更新商品
     * @param goodsVo
     */
    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        // 更新商品基本信息
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");  // 该商品如果被打回来，重新审核（将审核状态重置为待审核的状态）
        goodsDao.updateByPrimaryKeySelective(goods);
        // 更新商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);
        // 更新商品库存信息
        // 先删除
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(itemQuery);
        // 再插入
        if("1".equals(goods.getIsEnableSpec())){    // 启用规格：一个商品对应多个库存
            // 商品对应的库存
            List<Item> itemList = goodsVo.getItemList();
            if(itemList != null && itemList.size() > 0){
                for (Item item : itemList) {
                    // 设置库存的标题=spu名称+spu副标题+规格名称
                    String title = goods.getGoodsName() + " " + goods.getCaption();
                    // 规格名称：
                    // 栗子：{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();
                    Map<String, String> map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    item.setTitle(title);
                    setAttributeForItem(goods, goodsDesc, item);
                    // 保存库存
                    itemDao.insertSelective(item);
                }
            }
        }else{  // 不启用规格：一个商品对应一个库存
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());
            item.setIsDefault("1"); // 默认的库存信息
            item.setSpec("{}");     // 无规格
            item.setPrice(goods.getPrice());
            item.setNum(9999);      // 库存量
            setAttributeForItem(goods, goodsDesc, item);
            // 保存库存
            itemDao.insertSelective(item);
        }
    }

    /**
     * 商家系统下的商品的列表查询
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult searchForManager(Integer page, Integer rows, Goods goods) {
        // 设置分页条件
        PageHelper.startPage(page, rows);
        // 设置查询条件：待审核、未删除
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if(goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus().trim())){
            criteria.andAuditStatusEqualTo(goods.getAuditStatus().trim());  // 待审核
        }
        // 数据库的设计中：null：（0）未删除  1：已删除
        criteria.andIsDeleteIsNull();   // is null查询：MySQL不建议
        goodsQuery.setOrderByClause("id desc");
        // 根据条件查询
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 商品审核
     * @param ids
     * @param auditStatus
     */
    @Transactional
    @Override
    public void updateStatus(Long[] ids, String auditStatus) {
        if(ids != null && ids.length > 0){
            Goods goods = new Goods();
            goods.setAuditStatus(auditStatus);
            for (final Long id : ids) {
                // 更新商品的审核状态
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
                if("1".equals(auditStatus)){
                    // 将商品信息保存到索引库-上架
//                    isShow(id);
                    // 为了检索测试数据：将库存的数据全部保存到索引库中（并不是最终的程序）
//                    dataImportToSolr();
                    // 生成该商品详情的静态页面
//                    staticPageService.getHtml(id);
                    // 将商品id发送到mq中
                    jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
                        @Override
                        public Message createMessage(Session session) throws JMSException {
                            // 将id封装成消息体：文本消息、map消息
                            TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                            return textMessage;
                        }
                    });
                }
            }
        }
    }

    // 将该商品对应的库存保存到索引库中
    private void isShow(Long id) {
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1")
                .andIsDefaultEqualTo("1").andNumGreaterThan(0);
        List<Item> items = itemDao.selectByExample(itemQuery);
        if(items != null && items.size() > 0){
            // 手动的去处理动态字段
            for (Item item : items) {
                // json串：{"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    // 将库存的数据保存到索引库中
    private void dataImportToSolr() {
        // 查询所有可以的库存
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andStatusEqualTo("1");
        List<Item> items = itemDao.selectByExample(itemQuery);
        if(items != null && items.size() > 0){
            // 手动的去处理动态字段
            for (Item item : items) {
                // json串：{"机身内存":"16G","网络":"联通3G"}
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }

    /**
     * 商品的删除
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0){
            Goods goods = new Goods();
            goods.setIsDelete("1"); // 逻辑删除：1，删除状态
            for (final Long id : ids) {
                // 逻辑删除
                goods.setId(id);
                goodsDao.updateByPrimaryKeySelective(goods);
                // 从索引库中删除商品信息
//                SimpleQuery query = new SimpleQuery("item_goodsid:"+id);
//                solrTemplate.delete(query);
//                solrTemplate.commit();
                // 将商品id发送到mq中
                jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        // 封装消息体
                        TextMessage textMessage = session.createTextMessage(String.valueOf(id));
                        return textMessage;
                    }
                });
                // 删除静态页【可选】：不删除

            }
        }
    }

    // 设置库存的公共的属性
    private void setAttributeForItem(Goods goods, GoodsDesc goodsDesc, Item item){
        // 设置库存的图片
        // 栗子：[{"color":"灰色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSH2ABS1yAAh0T6sNTaI610.jpg"},{"color":"灰色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSJGALjVYAAPzuN901mo975.jpg"},{"color":"灰色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSJ6ARZShAAOxPhR4epI871.jpg"},{"color":"灰色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSKeAeYH6AARqxrS1vQY435.jpg"},{"color":"灰色","url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSLKAYbeGAAm3IpFnIFU096.jpg"},
        // {"url":"http://192.168.200.128/group1/M00/00/01/wKjIgFrUSLqAE7jjAALFbaIpUAs688.jpg"}]
        String images = goodsDesc.getItemImages();
        List<Map> list = JSON.parseArray(images, Map.class);
        if(list != null && list.size() > 0){
            // 取出一张图片
            String image = list.get(0).get("url").toString();
            item.setImage(image);
        }
        item.setCategoryid(goods.getCategory3Id()); // 三级分类id
        item.setStatus("1");    // 状态
        item.setGoodsId(goods.getId()); // 商品id
        item.setSellerId(goods.getSellerId());  // 商家id
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
        item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName()); // 分类名称
        item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());    // 品牌名称
        item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getNickName());   // 店铺名称
    }
}
