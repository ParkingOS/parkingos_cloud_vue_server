package parkingos.com.bolink.dao.mybatis.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.models.OrderTb;

import java.util.List;
import java.util.Map;
@Mapper
public interface OrderMapper {
    @Select("select cityid from com_info_tb where id =#{comid} and state=0")
    Long getCityIdByComId(@Param("comid")Long comid);

    @Select("select cityid from org_group_tb where id =#{groupid} and state=0")
    Long getCityIdByGroupId(@Param("groupid") Long groupid);

    @Select("select groupid from com_info_tb where id =#{comid} and state=0")
    Long getGroupIdByComId(@Param("comid")Long comid);

    List<OrderTb> selectOrders(OrderTbExample example);

    int selectOrdersCount(OrderTbExample example);

    List<Map<String,Object>> qryOrdersByComidAndOrderId(@Param("comid") Long comid, @Param("orderIdLocal") String orderIdLocal, @Param("tableName") String tableName);

    Map<String,Object> getParkNameById(long comid);

    String getUserInfo(long l);

    @Select("select id from com_info_tb where groupid =#{groupid} and state<>1")
    List<Long> getComlistByGroupid(@Param("groupid")Long groupid);
}