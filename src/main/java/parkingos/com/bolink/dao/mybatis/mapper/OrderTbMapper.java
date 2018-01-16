package parkingos.com.bolink.dao.mybatis.mapper;


import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.models.OrderTb;

import java.util.List;

public interface OrderTbMapper {
    int countByExample(OrderTbExample example);

    int deleteByExample(OrderTbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderTb record);

    int insertSelective(OrderTb record);

    List<OrderTb> selectByExample(OrderTbExample example);

    OrderTb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderTb record, @Param("example") OrderTbExample example);

    int updateByExample(@Param("record") OrderTb record, @Param("example") OrderTbExample example);

    int updateByPrimaryKeySelective(OrderTb record);

    int updateByPrimaryKey(OrderTb record);
}