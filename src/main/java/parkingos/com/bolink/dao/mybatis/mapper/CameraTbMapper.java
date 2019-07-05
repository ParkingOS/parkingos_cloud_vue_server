package parkingos.com.bolink.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.dao.mybatis.CameraTbExample;
import parkingos.com.bolink.models.CameraTb;

import java.util.List;
@Mapper
public interface CameraTbMapper {
    int countByExample(CameraTbExample example);

    int deleteByExample(CameraTbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CameraTb record);

    int insertSelective(CameraTb record);

    List<CameraTb> selectByExample(CameraTbExample example);

    CameraTb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CameraTb record, @Param("example") CameraTbExample example);

    int updateByExample(@Param("record") CameraTb record, @Param("example") CameraTbExample example);

    int updateByPrimaryKeySelective(CameraTb record);

    int updateByPrimaryKey(CameraTb record);
}