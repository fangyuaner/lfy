package cn.itcast.core.service.content;

import java.util.List;

import cn.itcast.core.entity.PageResult;
import cn.itcast.core.pojo.ad.ContentQuery;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.itcast.core.dao.ad.ContentDao;
import cn.itcast.core.pojo.ad.Content;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Service
public class ContentServiceImpl implements ContentService {

	@Resource
	private ContentDao contentDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

	@Override
	public List<Content> findAll() {
		List<Content> list = contentDao.selectByExample(null);
		return list;
	}

	@Override
	public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<Content> page = (Page<Content>)contentDao.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(Content content) {
	    // 新增时，需要更新缓存
        clearCache(content.getCategoryId());    // 清空缓存
        // 将数据查询出来在放入缓存：自己去完成一下     // 定时去完成（解决方案：qurate、Scheduled）--- day11 补充
		contentDao.insertSelective(content);
	}

	// 清空缓存
    private void clearCache(Long categoryId) {
	    redisTemplate.boundHashOps("content").delete(categoryId);
    }

    @Override
	public void edit(Content content) {
        // 更新时，需要更新缓存
        // 判断分类id是否发生改变
        Long newCategoryId = content.getCategoryId();   // 本次提交的分类id
        Long oldCategoryId = contentDao.selectByPrimaryKey(content.getId()).getCategoryId();// 原始的分类id
        if(newCategoryId != oldCategoryId){
            // 分类发生改变
            clearCache(newCategoryId);
            clearCache(oldCategoryId);
        }else{
            clearCache(oldCategoryId);
        }
        contentDao.updateByPrimaryKeySelective(content);
	}

	@Override
	public Content findOne(Long id) {
		Content content = contentDao.selectByPrimaryKey(id);
		return content;
	}

	@Override
	public void delAll(Long[] ids) {
		if(ids != null){
			for(Long id : ids){
			    // 删除时，需要更新缓存
                clearCache(contentDao.selectByPrimaryKey(id).getCategoryId());
				contentDao.deleteByPrimaryKey(id);
			}
		}
	}

	/**
	 * 前台系统首页大广告的轮播
	 * @param categoryId
	 * @return
	 */
    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        // 先从缓存中获取数据
        List<Content> list = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
        // 判断缓存是否存在
        if(list == null){
            // 缓存的穿透（击穿）
            synchronized (this){
                list = (List<Content>) redisTemplate.boundHashOps("content").get(categoryId);
                if(list == null){
                    // 从数据中查询
                    // 设置条件
                    ContentQuery contentQuery = new ContentQuery();
                    // 根分类id并且是可用的
                    contentQuery.createCriteria().andCategoryIdEqualTo(categoryId).
                            andStatusEqualTo("1");
                    contentQuery.setOrderByClause("sort_order desc"); // 排序
                    list = contentDao.selectByExample(contentQuery);
                    // 加入缓存
                    redisTemplate.boundHashOps("content").put(categoryId, list);
                }
            }
        }
        return list;
    }


}
