package parkingos.com.bolink.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GroupLogMapper {

    int getCount(@Param("groupId") Long groupid, @Param("id")Long logId, @Param("operateType")Integer operateType, @Param("parkId")Long parkId, @Param("type")String typeStart, @Param("remark")String remark, @Param("timeStart")Long timeStart,  @Param("timeEnd")Long timeEnd);

    List<Map<String,Object>> getRows(@Param("groupId") Long groupid, @Param("id")Long logId, @Param("operateType")Integer operateType, @Param("parkId")Long parkId, @Param("type")String typeStart, @Param("remark")String remark, @Param("timeStart")Long timeStart,  @Param("timeEnd")Long timeEnd,  @Param("pageSize")Integer rp, @Param("offset")Integer offset);
}
