package cn.itcast.core.service.brand;

import cn.itcast.core.dao.good.BrandDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.good.Brand;
import cn.itcast.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

//    @Autowired

    // 好处：
    // 框架对外提供的服务越多，性能肯定越低
    // 降低与框架间的耦合度
    @Resource
    private BrandDao brandDao;

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<Brand> findAll() {
        // selectByExample：根据条件查询
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        // 1、设置分页条件
        PageHelper.startPage(pageNo, pageSize);
        // 2、根据条件查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);
        // 问题：不直接返回page/pageinfo：对象越大，传输的效率越低
        // 3、将结果封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 条件查询
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand) {
        // 1、设置分页条件
        PageHelper.startPage(pageNo, pageSize);
        // 2、封装查询条件
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria();
        if (brand.getName() != null && !"".equals(brand.getName().trim())){
            criteria.andNameLike("%"+brand.getName()+"%");
        }
        if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar().trim())){
            criteria.andFirstCharEqualTo(brand.getFirstChar());
        }
        // 根据id降序
        brandQuery.setOrderByClause("id desc");
        // 3、根据条件查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        // 4、将结果封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 保存品牌
     * @param brand
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * 回显品牌
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * 更新品牌
     * @param brand
     */
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        // 报表数据导入到数据库中
        // 前端：校验仅仅只是用来提高用户的体验，不能保证数据的安全性
        // 数据的安全性：必须在服务端校验（p2p）
        if(ids != null && ids.length > 0){
            // 效率：不高
//            for (Long id : ids) {
//                brandDao.deleteByPrimaryKey(id);
//            }
            // 批量删除
            brandDao.deleteByPrimaryKeys(ids);
        }
    }

    /**
     * 新增模板时加载品牌结果集
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
