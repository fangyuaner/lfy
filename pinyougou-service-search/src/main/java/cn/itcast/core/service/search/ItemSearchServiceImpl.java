package cn.itcast.core.service.search;

import cn.itcast.core.dao.item.ItemDao;
import cn.itcast.core.pojo.item.Item;
import cn.itcast.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.apache.solr.common.util.Hash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Resource
    private SolrTemplate solrTemplate;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ItemDao itemDao;

    /**
     * 前台系统检索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        // 创建一个大的map，封装所有的结果集‘
        Map<String, Object> resultMap = new HashMap<>();
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            // trim：去前后空格。可以自动去掉。angularjs自动去掉前后的空格
            keywords = keywords.replace(" ","");
            searchMap.put("keywords", keywords);
        }
        // 1、根据关键字检索并且分页
//        Map<String, Object> map = searchForPage(searchMap);
//        resultMap.putAll(map);
        Map<String, Object> map = searchForHigtLightPage(searchMap);
        resultMap.putAll(map);
        // 2、加载商品分类列表
        List<String> categoryList = searchForGroupPage(searchMap);
        resultMap.put("categoryList", categoryList);
        // 3、默认加载第一个分类下的商品品牌、规格列表
        if(categoryList != null && categoryList.size() > 0){
            Map<String, Object> brandAndSpecMap = defaultSelectBrandsAndSpecsByCategoryName(categoryList.get(0));
            resultMap.putAll(brandAndSpecMap);
        }
//        return map;
        return resultMap;
    }

    @Override
    public void isShow(Long id) {
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

    /**
     * 商品下架
     * @param id
     */
    @Override
    public void deleteItemFromSolr(Long id) {
        SimpleQuery query = new SimpleQuery("item_goodsid:"+id);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    // 默认加载第一个分类下的商品品牌、规格列表
    private Map<String,Object> defaultSelectBrandsAndSpecsByCategoryName(String categoryName) {
        // 通过分类名称获取模板id
        Object typeId = redisTemplate.boundHashOps("itemList").get(categoryName);
        // 通过模板id品牌结果集
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        // 通过模板id品牌规格集
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);
        // 封装结果
        Map<String,Object> map = new HashMap<>();
        map.put("brandList", brandList);
        map.put("specList", specList);
        return map;
    }

    // 加载商品分类
    private List<String> searchForGroupPage(Map<String, String> searchMap) {
        // 1、封装检索的条件
        Criteria criteria = new Criteria("item_keywords");  // 指定根据哪个字段检索
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);  // is方法：对检索的内容切词（词条）
        }
        // 2、设置分组条件
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");  // 根据哪个字段进行分组
        query.setGroupOptions(groupOptions);
        // 3、根据条件查询
        List<String> list = new ArrayList<>();
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue(); // 数据
            list.add(groupValue);
        }
        // 4、将结果封装到list中
        return list;

    }

    // 根据关键字检索并且分页以及关键字高亮
    private Map<String,Object> searchForHigtLightPage(Map<String, String> searchMap) {
        // 1、封装检索的条件
        Criteria criteria = new Criteria("item_keywords");  // 指定根据哪个字段检索  where filename like ?
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);  // is方法：对检索的内容切词（词条）
        }
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        // 2、封装分页的条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer startRow = (pageNo - 1) * pageSize;
        query.setOffset(startRow);  // 其始行
        query.setRows(pageSize);    // 每页显示的条数
        // 3、封装高亮的条件
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");    // 添加对该字段中包含的关键字进行高亮
        highlightOptions.setSimplePrefix("<font color='red'>");
        highlightOptions.setSimplePostfix("</font>");
        query.setHighlightOptions(highlightOptions);
        // select * from table where keywords like ?
        // 过滤：继续封装过滤的条件
        // 分类过滤
        String category = searchMap.get("category");
        if(category != null && !"".equals(category)){
            Criteria cri = new Criteria("item_category");
            cri.is(category);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }
        // 品牌过滤
        String brand = searchMap.get("brand");
        if(brand != null && !"".equals(brand)){
            Criteria cri = new Criteria("item_brand");
            cri.is(brand);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }
        // 规格过滤
        String spec = searchMap.get("spec");
        if(spec != null && !"".equals(spec)){
            // spec数据：{内存：128G,网络：4G}
            Map<String, String> map = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                Criteria cri = new Criteria("item_spec_"+entry.getKey());
                cri.is(entry.getValue());
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
                query.addFilterQuery(filterQuery);
            }
        }
        // 价格过滤
        String price = searchMap.get("price");
        if(price != null && !"".equals(price)){
            // 页面传递的价格：区间段[min-max]  xxx以上 [xxx-*]
            // between   >=
            String[] prices = price.split("-");
            Criteria cri = new Criteria("item_price");
            if(price.contains("*")){    // 以上
                cri.greaterThanEqual(prices[0]);
            }else{  // 区间段
                cri.between(prices[0], prices[1], true, true);
            }
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }
        // select * from table where keywords like ? and field1 like ? and field2 like ? ....

        // 继续封装条件：排序
        String sort = searchMap.get("sort");
        if(sort != null && !"".equals(sort)){
            if("ASC".equals(sort)){ // 升序
                Sort s = new Sort(Sort.Direction.ASC, "item_" + searchMap.get("sortField"));
                query.addSort(s);
            }else{
                Sort s = new Sort(Sort.Direction.DESC, "item_" + searchMap.get("sortField"));
                query.addSort(s);
            }
        }

        // 4、根据条件检索
        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);
        // 封装高亮的结果
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        if(highlighted != null && highlighted.size() > 0){
            for (HighlightEntry<Item> highlightEntry : highlighted) {
                Item item = highlightEntry.getEntity(); // 普通的结果
                List<HighlightEntry.Highlight> highlights = highlightEntry.getHighlights();
                if(highlights != null && highlights.size() > 0){
                    // 取出高亮的title
                    String title = highlights.get(0).getSnipplets().get(0);
                    item.setTitle(title);
                }
            }
        }


        // 5、将结果封装map
        Map<String,Object> map = new HashMap<>();
        map.put("totalPages", highlightPage.getTotalPages());  // 总页数
        map.put("total", highlightPage.getTotalElements());    // 总条数
        map.put("rows", highlightPage.getContent());           // 商品列表
        return map;
    }

    // 根据关键字检索并且分页
    private Map<String,Object> searchForPage(Map<String, String> searchMap) {
        // 1、封装检索的条件
        Criteria criteria = new Criteria("item_keywords");  // 指定根据哪个字段检索  where filename like ?
        String keywords = searchMap.get("keywords");
        if(keywords != null && !"".equals(keywords)){
            criteria.is(keywords);  // is方法：对检索的内容切词（词条）
        }
        SimpleQuery query = new SimpleQuery(criteria);
        // 2、封装分页的条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer startRow = (pageNo - 1) * pageSize;
        query.setOffset(startRow);  // 其始行
        query.setRows(pageSize);    // 每页显示的条数
        // 3、根据条件检索
        ScoredPage<Item> scoredPage = solrTemplate.queryForPage(query, Item.class);
        // 4、将结果封装map
        Map<String,Object> map = new HashMap<>();
        map.put("totalPages", scoredPage.getTotalPages());  // 总页数
        map.put("total", scoredPage.getTotalElements());    // 总条数
        map.put("rows", scoredPage.getContent());           // 商品列表
        return map;
    }
}
