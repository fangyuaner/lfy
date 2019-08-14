package cn.itcast.core.task;

import cn.itcast.core.dao.item.ItemCatDao;
import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.pojo.item.ItemCat;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RedisTask {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemCatDao itemCatDao;

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    // corn：指定该方法的执行的时间规则
    @Scheduled(cron = "30 35 18 28 02 *")
    public void autoItemCatToRedis(){
        List<ItemCat> list = itemCatDao.selectByExample(null);
        if(list != null && list.size() > 0){
            for (ItemCat itemCat : list) {
                redisTemplate.boundHashOps("itemList").put(itemCat.getName(), itemCat.getTypeId());
            }
            System.out.println("定时器执行了。。。1");
        }
    }

    @Scheduled(cron = "30 35 18 28 02 *")
    public void autoBrandsAndSpecsToRedis(){
        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if(list != null && list.size() > 0){
            for (TypeTemplate template : list) {
                // 缓存品牌结果集
                String brandIds = template.getBrandIds();
                List<Map> brandList = JSON.parseArray(brandIds, Map.class);
                redisTemplate.boundHashOps("brandList").put(template.getId(), brandList);
                // 缓存规格结果集
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(), specList);
            }
            System.out.println("定时器执行了。。。2");
        }
    }

    public List<Map> findBySpecList(Long id) {
        // 根据模板id获取规格选项
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        // 栗子：[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        // 将json串转成对象
        List<Map> specList = JSON.parseArray(specIds, Map.class);
        // 设置规格选项结果集
        if(specList != null && specList.size() > 0){
            for (Map map : specList) {
                // 获取规格id
                Long specId = Long.parseLong(map.get("id").toString());
                // 获取对应的规格选项
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                query.createCriteria().andSpecIdEqualTo(specId);
                List<SpecificationOption> options = specificationOptionDao.selectByExample(query);
                // 将规格选项设置到map中
                map.put("options", options);
            }
        }
        return specList;
    }
}
