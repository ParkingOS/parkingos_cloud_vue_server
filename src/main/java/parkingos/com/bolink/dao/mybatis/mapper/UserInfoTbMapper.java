package parkingos.com.bolink.dao.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.models.UserInfoTbExample;

@Mapper
public interface UserInfoTbMapper {
    int countByExample(UserInfoTbExample example);

    int deleteByExample(UserInfoTbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserInfoTb record);

    int insertSelective(UserInfoTb record);

    List<UserInfoTb> selectByExample(UserInfoTbExample example);

    UserInfoTb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserInfoTb record, @Param("example") UserInfoTbExample example);

    int updateByExample(@Param("record") UserInfoTb record, @Param("example") UserInfoTbExample example);

    int updateByPrimaryKeySelective(UserInfoTb record);

    int updateByPrimaryKey(UserInfoTb record);
}