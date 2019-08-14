package cn.itcast.core.service.staticpage;

import cn.itcast.core.dao.good.GoodsDao;
import cn.itcast.core.dao.good.GoodsDescDao;
import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.good.GoodsDesc;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticPageServiceImpl implements StaticPageService,ServletContextAware {

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private ItemDao itemDao;

    // 注入FreeMarkerConfigurer，带来的好处
    // 1、获取到Configuration
    // 2、指定模板的位置
    private Configuration configuration;
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    /**
     * 生成静态页面
     * @param id 静态页名称
     */
    @Override
    public void getHtml(Long id) {

        try {
            // 1、创建Configuration并且指定模板的位置
            // new Configuration  setDirectoryForTemplatePath() 此路不通
            // 想办法：参考一下springmvc，指定视图的地址在配置文件中配置的
            // 2、获取该位置下需要的模板
            Template template = configuration.getTemplate("item.ftl");
            // 3、准备业务数据
            Map<String, Object> dataModel = getDataModel(id);
            // 指定file
            // 静态页生成的位置：可以直接访问---项目发布的真实路径
//            request.getRealPath(xxx); 此路不通
            String pathname = "/" + id + ".html";
            String path = servletContext.getRealPath(pathname);
            File file = new File(path); // 静态页生成的位置
            // 4、模板 + 数据 = 输出
            template.process(dataModel, new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 静态页面需要的数据
    private Map<String,Object> getDataModel(Long id) {
        Map<String,Object> map = new HashMap<>();
        // 1、商品副标题
        Goods goods = goodsDao.selectByPrimaryKey(id);
        map.put("goods", goods);
        // 2、商品图片、介绍等
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        map.put("goodsDesc", goodsDesc);
        // 3、商品的分类
        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());
        ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());
        ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
        map.put("itemCat1", itemCat1);
        map.put("itemCat2", itemCat2);
        map.put("itemCat3", itemCat3);
        // 4、商品对应的库存
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id).
                andStatusEqualTo("1").andNumGreaterThan(0);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        map.put("itemList", itemList);
        return map;
    }


}
