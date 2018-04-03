package parkingos.com.bolink.dao.mybatis.mapper;

import com.mongodb.util.Hash;
import org.apache.ibatis.annotations.Param;
import java.util.HashMap;
import java.util.List;

public interface ParkInfoMapper {
    List<HashMap<String,Object>> getEntryCar(@Param("create_time") long create_time, @Param("groupid")long groupid );
    List<HashMap<String,Object>> getExitCar( @Param("create_time") long create_time, @Param("groupid")long groupid);
    List<HashMap<String,Object>> getParkRank(@Param("end_time") long create_time, @Param("groupid")long groupid);
    int getBerthTotal(@Param("groupid") int groupid);
    double getElectronicPay(@Param("create_time") long create_time, @Param("groupid")int groupid);
    double getCashPay(@Param("create_time") long create_time, @Param("groupid")int groupid);
    double getReduceAmount(@Param("create_time") long create_time, @Param("groupid")int groupid);
    int getEntryCount(@Param("create_time") long create_time, @Param("groupid")int groupid);
    int getExitCount(@Param("end_time") long end_time, @Param("groupid")int groupid);
    int getInparkCount(@Param("create_time") long create_time,@Param("groupid")int groupid);
    List<HashMap<String,Object>> getParkIdByGroupId(@Param("groupid") int groupid);
    List<HashMap<String,Object>> getParkLogin(@Param("parkid")String parkid);
    List<HashMap<String,Object>> getExpByGid(@Param("groupid") int groupid,@Param("ctime")long ctime);
    List<HashMap<String,Object>> getExpByCid(@Param("comid") int comid,@Param("ctime")long ctime);

    List<HashMap<String,Object>> getEntryCarByComid(@Param("create_time") long create_time, @Param("comid")int comid );
    List<HashMap<String,Object>> getExitCarByComid( @Param("create_time") long create_time, @Param("comid")int comid);
    List<HashMap<String,Object>> getRankByout(@Param("end_time") long create_time, @Param("comid")int comid);
    double getElectronicPaybc(@Param("create_time") long create_time, @Param("comid")int comid);
    double getCashPaybc(@Param("create_time") long create_time, @Param("comid")int comid);
    double getReduceAmountbc(@Param("create_time") long create_time, @Param("comid")int comid);
    int getEntryCountbc(@Param("create_time") long create_time, @Param("comid")int comid);
    int getExitCountbc(@Param("end_time") long end_time, @Param("comid")int comid);
    int getInparkCountbc(@Param("create_time") long create_time,@Param("comid")int comid);
    int getBerthTotalbc(@Param("comid") int comid);
    String getUserInfo(@Param("id") long id);
    double getFreeAmount(@Param("create_time") long create_time, @Param("groupid")int groupid);
    double getFreeAmountbc(@Param("create_time") long create_time, @Param("comid")int comid);
    List<HashMap<String,Object>> getBerthPercent(@Param("comidlist") List<HashMap<String,Object>> list,@Param("create_time") long create_time);

}
