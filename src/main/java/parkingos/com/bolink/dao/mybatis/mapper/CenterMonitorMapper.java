package parkingos.com.bolink.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
public interface CenterMonitorMapper {
    List<HashMap<String,Object>> getConfirmOrdersByGroupid( @Param("groupid") Long groupid);

    List<HashMap<String,Object>> getConfirmOrdersByComid( @Param("comid") String comid);

    Map getPicMap(@Param("eventid")String eventId, @Param("comid")String comid);

    List<Map<String,Object>> getCarByNameLike(@Param("comid") long comid, @Param("carnumlist") List<String> list,@Param("tableName") String tableName);

    Map matchPicMap(@Param("orderid")String orderid, @Param("comid")String comid);

    Map getSelectOrder(@Param("comid")long comid, @Param("carNumber")String carNumber,@Param("tableName") String tableName);

    Map getConfirmOrder(@Param("eventid")String event_id, @Param("comid")String comid);

    Map getLiftRodInfo(@Param("channelid")String channel_id, @Param("comid")String comid);

    Map<String,Object> getMonitorMap(@Param("calleridnum")Long callerid_num);

    List<Map<String,Object>> getMonitorsByGroupid(@Param("groupid")Long groupid);

    List<Map<String,Object>> getMonitorsByComid(@Param("comid")String comid);

    Map qryChannelByMonitId(@Param("monitorid")Long monitor_id);
}
