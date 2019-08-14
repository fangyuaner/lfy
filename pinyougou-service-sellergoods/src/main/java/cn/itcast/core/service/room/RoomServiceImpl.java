package cn.itcast.core.service.room;

import cn.itcast.core.dao.room.RoomDao;
import cn.itcast.core.pojo.room.Room;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Resource
    private RoomDao roomDao;

    @Transactional
    @Override
    public void add(Room room) {
        roomDao.insertSelective(room);
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = roomDao.selectByExample(null);
        return rooms;

    }

     //批量删除
    @Transactional
    @Override
    public void delete(Long[] ids) {
        // 报表数据导入到数据库中
        // 前端：校验仅仅只是用来提高用户的体验，不能保证数据的安全性
        // 数据的安全性：必须在服务端校验（p2p）
        if(ids != null && ids.length > 0){
            // 批量删除
            roomDao.deleteByPrimaryKeys(ids);
        }
    }
}
