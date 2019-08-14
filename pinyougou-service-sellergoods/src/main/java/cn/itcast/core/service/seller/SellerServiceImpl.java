package cn.itcast.core.service.seller;

import cn.itcast.core.dao.seller.SellerDao;
import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.seller.Seller;
import cn.itcast.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SellerServiceImpl implements SellerService {

    @Resource
    private SellerDao sellerDao;

    /**
     * 商家的入驻申请
     * @param seller
     */
    @Transactional
    @Override
    public void add(Seller seller) {
        // 初始化商家的审核状态：待审核
        seller.setStatus("0");
        seller.setCreateTime(new Date());
        // 对密码加密：md5、spring加盐、BCrypt-加盐（盐值）
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        sellerDao.insertSelective(seller);
    }

    /**
     * 查询待审核的商家列表
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        PageHelper.startPage(page, rows);
        SellerQuery sellerQuery = new SellerQuery();
        sellerQuery.createCriteria().andStatusEqualTo(seller.getStatus());
        Page<Seller> p = (Page<Seller>) sellerDao.selectByExample(sellerQuery);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 回显商家信息
     * @param sellerId
     * @return
     */
    @Override
    public Seller findOne(String sellerId) {
        return sellerDao.selectByPrimaryKey(sellerId);
    }

    /**
     * 审核商家
     * @param sellerId
     * @param status
     */
    @Transactional
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setSellerId(sellerId);
        seller.setStatus(status);
        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
