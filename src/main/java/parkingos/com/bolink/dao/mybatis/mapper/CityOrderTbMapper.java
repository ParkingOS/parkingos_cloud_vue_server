package parkingos.com.bolink.dao.mybatis.mapper;


import org.apache.ibatis.annotations.Mapper;
import parkingos.com.bolink.dao.mybatis.OrderAndParkTbExample;
import parkingos.com.bolink.models.OrderAndParkTb;

import java.util.List;
@Mapper
public interface CityOrderTbMapper {
//    List<OrderAndParkTb> queryByParam(OrderAndParkTb orderAndParkTb);

    int countByExample(OrderAndParkTbExample example);

    List<OrderAndParkTb> selectByExample(OrderAndParkTbExample example);
    List<OrderAndParkTb> queryByParam(List<Long> list);
}