package cn.itcast.core.service.temp;

import cn.itcast.core.dao.specification.SpecificationOptionDao;
import cn.itcast.core.dao.template.TypeTemplateDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.specification.SpecificationOption;
import cn.itcast.core.pojo.specification.SpecificationOptionQuery;
import cn.itcast.core.pojo.template.TypeTemplate;
import cn.itcast.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 模板的列表查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {

        // 查询模板列表的时候将数据写到内存中
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
        }

        // 1、设置分页条件
        PageHelper.startPage(page, rows);
        // 2、设置查询条件
        TypeTemplateQuery query = new TypeTemplateQuery();
        if(typeTemplate.getName() != null && !"".equals(typeTemplate.getName().trim())){
            query.createCriteria().andNameLike("%" + typeTemplate.getName().trim() + "%");
        }
        query.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<TypeTemplate> p = (Page<TypeTemplate>) typeTemplateDao.selectByExample(query);
        // 4、封装结果
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 新增模板
     * @param typeTemplate
     */
    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }

    /**
     * 新增商品：加载品牌以及扩展属性
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findOne(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    /**
     * 新增商品：加载规格以及规格选项
     * @param id
     * @return
     */
    @Override
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
