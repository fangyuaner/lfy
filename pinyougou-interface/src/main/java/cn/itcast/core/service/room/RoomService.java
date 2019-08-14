package cn.itcast.core.service.room;

import cn.itcast.core.pojo.room.Room;

import java.util.List;

public interface RoomService {

    //新增会议室
    void add(Room room);

    //查询所有
    List<Room> findAll();

    //删除资源
    public void delete(Long[] ids);
}
