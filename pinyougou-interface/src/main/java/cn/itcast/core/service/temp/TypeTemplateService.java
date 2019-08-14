package cn.itcast.core.service.temp;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.template.TypeTemplate;
import freemarker.template.Template;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService {

    /**
     * 模板的列表查询
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);

    /**
     * 新增模板
     * @param typeTemplate
     */
    public void add(TypeTemplate typeTemplate);

    /**
     * 新增商品：加载品牌以及扩展属性
     * @param id
     * @return
     */
    public TypeTemplate findOne(Long id);

    /**
     * 新增商品：加载规格以及规格选项
     * @param id
     * @return
     */
    public List<Map> findBySpecList(Long id);

}
