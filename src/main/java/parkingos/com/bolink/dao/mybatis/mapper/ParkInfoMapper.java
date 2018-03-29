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


}
