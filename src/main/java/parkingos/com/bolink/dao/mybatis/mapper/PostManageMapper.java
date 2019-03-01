package parkingos.com.bolink.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostManageMapper {

    List<Map<String,Object>> getChannels(@Param("comid") Long comid,@Param("limit") Integer rp,@Param("offset") Integer offset);

    int getChannelsCount(@Param("comid")Long comid);
}
