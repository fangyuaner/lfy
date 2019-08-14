package cn.itcast.core.service.uszer;

import cn.itcast.core.dao.uszer.UszerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.uszer.Uszer;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UszerServiceImpl implements UszerService {

    //不使用Autowired的好处:框架对外提供的服务越多,性能越低
    //降低了与框架间的耦合度
    @Resource
    private UszerDao uszerDao;


    //新增用户
    @Transactional
    @Override
    public void add(Uszer uszer) {
        //初始化商家的审核状态
        uszer.setStatus("0");
        //对密码进行加密--使用BCript加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(uszer.getPassword());
        uszer.setPassword(encode);

        uszerDao.insertSelective(uszer);
    }


    // 查询所有用户
    public List<Uszer> findAll() {
        //根据条件查询
        List<Uszer> uszers = uszerDao.selectByExample(null);
        return uszers;
    }

    //查询单个用户
    public Uszer findOne(Integer id) {
        Uszer uszer = uszerDao.selectByPrimaryKey(id);
        return uszer;
    }

    @Override
    public Uszer findOneByUsername(String username) {
        Uszer uszer = uszerDao.selectByUsername(username);
        return uszer;
    }


    //修改资料
    @Transactional
    @Override
    public void update(Uszer uszer) {
        uszerDao.updateByPrimaryKeySelective(uszer);
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
        Page<Uszer> page = (Page<Uszer>) uszerDao.selectByExample(null);
        // 问题：不直接返回page/pageinfo：对象越大，传输的效率越低
        // 3、将结果封装到PageResult中
        return new PageResult(page.getTotal(), page.getResult());
    }


}
