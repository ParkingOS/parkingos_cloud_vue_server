package parkingos.com.bolink.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface BolinkDataMapper {

    List<Map<String,Object>> getTransactions(@Param("table_name") String bolinkTableName, @Param("b_time") Long btime, @Param("e_time")Long etime,@Param("comid")Long comid);

    List<Map<String,Object>> getTransactionsByUid(@Param("table_name") String bolinkTableName, @Param("b_time")Long start,  @Param("e_time")Long end, @Param("comid")Long comid, @Param("userId")long userId);

    List<Map<String,Object>> getTransactionsByAllUid(@Param("table_name")String bolinkTableName, @Param("b_time")Long start, @Param("e_time")Long end, @Param("comid")Long comid);

    List<Map<String,Object>> getOrderMoneys(@Param("table_name")String tableName, @Param("orderId")String orderid, @Param("comid")Long comid, @Param("carNumber")String carNumber);

    List<Map<String,Object>> getMonthTransactionsByComid(@Param("comid")Long comid,@Param("b_time") Long b, @Param("e_time")Long e, @Param("table_name")String bolinkTableName);

    List<Map<String,Object>> getMonthOutTransactionsByComid(@Param("comid")Long comid, @Param("b_time")Long b, @Param("e_time")Long e, @Param("table_name")String bolinkTableName);

    List<Map<String,Object>> getTransactionsByComid(@Param("table_name")String bolinkTableName, @Param("b_time")Long btime, @Param("e_time")Long etime, @Param("comid")long comid);

    List<Map<String,Object>> getTransactionsByGroupId(@Param("table_name")String bolinkTableName,@Param("b_time") Long btime, @Param("e_time")Long etime, @Param("groupId")Long groupid);

    List<Map<String,Object>> getOutTransactionsByComid(@Param("table_name")String bolinkTableName, @Param("b_time")Long btime, @Param("e_time")Long etime, @Param("comid")long comid);

    List<Map<String,Object>> getOutTransactionsByGroupId(@Param("table_name")String bolinkTableName, @Param("b_time")Long btime, @Param("e_time")Long etime, @Param("groupId")Long groupid);

    List<Map<String,Object>> getDailyTransactionsByGroupId(@Param("table_name")String bolinkTableName, @Param("b_time")Long btime, @Param("e_time")Long etime, @Param("groupId")Long groupid);

    List<Map<String,Object>> getDailyOutTransactionsByGroupId(@Param("table_name")String bolinkTableName, @Param("b_time")Long btime, @Param("e_time")Long etime, @Param("groupId")Long groupid);

    List<Map<String,Object>> getMonthTransactionsByGroupId(@Param("table_name")String bolinkTableName, @Param("b_time")Long b, @Param("e_time")Long e, @Param("groupId")Long groupid);

    List<Map<String,Object>> getMonthOutTransactionsByGroupId(@Param("table_name")String bolinkTableName, @Param("b_time")Long b, @Param("e_time")Long e, @Param("groupId")Long groupid);

    List<Map<String,Object>> getParkDaylyStaticAnls(@Param("comid")Long comid,@Param("b_time") Long btime, @Param("e_time")Long etime);

    List<Map<String,Object>> getParkMonthlyStaticAnls(@Param("comid")Long comid, @Param("b_time")Long btime, @Param("e_time")Long etime);

    List<Map<String,Object>> getGroupParksAnly(@Param("groupid")Long groupid, @Param("comid")Long comid,  @Param("b_time")Long btime, @Param("e_time")Long etime);

    List<Map<String,Object>> getGroupDaylyAnly(@Param("groupid")Long groupid,  @Param("b_time")Long btime, @Param("e_time")Long etime);

    List<Map<String,Object>> getGroupMonthLyAnly(@Param("groupid")Long groupid,  @Param("b_time")Long btime, @Param("e_time")Long etime);

    List<Map<String,Object>> getParkCollectorAnly(@Param("comid")Long comid, @Param("outUid")Long outCollector, @Param("b_time")Long start, @Param("e_time")Long end);

    List<String> getParkIdsByGroupId(@Param("groupid")Long id);

    Long getUnionServerIdByCloudId(@Param("id")Long serverId);
}
