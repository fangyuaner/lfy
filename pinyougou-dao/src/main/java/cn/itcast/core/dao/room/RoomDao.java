package cn.itcast.core.dao.room;

import cn.itcast.core.pojo.room.Room;
import cn.itcast.core.pojo.room.RoomQuery;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoomDao {
    int countByExample(RoomQuery example);

    int deleteByExample(RoomQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Room record);

    int insertSelective(Room record);

    List<Room> selectByExample(RoomQuery example);

    Room selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Room record, @Param("example") RoomQuery example);

    int updateByExample(@Param("record") Room record, @Param("example") RoomQuery example);

    int updateByPrimaryKeySelective(Room record);

    int updateByPrimaryKey(Room record);

    void deleteByPrimaryKeys(Long[] ids);

}