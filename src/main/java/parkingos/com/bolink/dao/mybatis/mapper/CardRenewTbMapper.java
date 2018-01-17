package parkingos.com.bolink.dao.mybatis.mapper;


import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.dao.mybatis.CardRenewTbExample;
import parkingos.com.bolink.models.CardRenewTb;

import java.util.List;

public interface CardRenewTbMapper {
    int countByExample(CardRenewTbExample example);

    int deleteByExample(CardRenewTbExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CardRenewTb record);

    int insertSelective(CardRenewTb record);

    List<CardRenewTb> selectByExample(CardRenewTbExample example);

    CardRenewTb selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CardRenewTb record, @Param("example") CardRenewTbExample example);

    int updateByExample(@Param("record") CardRenewTb record, @Param("example") CardRenewTbExample example);

    int updateByPrimaryKeySelective(CardRenewTb record);

    int updateByPrimaryKey(CardRenewTb record);
}