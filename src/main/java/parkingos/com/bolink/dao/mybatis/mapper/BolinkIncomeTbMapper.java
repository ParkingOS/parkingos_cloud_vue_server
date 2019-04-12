package parkingos.com.bolink.dao.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.dao.mybatis.BolinkIncomeTbExample;
import parkingos.com.bolink.models.BolinkIncomeTb;
@Mapper
public interface BolinkIncomeTbMapper {
    int countByExample(BolinkIncomeTbExample example);

    int deleteByExample(BolinkIncomeTbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BolinkIncomeTb record);

    int insertSelective(BolinkIncomeTb record);

    List<BolinkIncomeTb> selectByExample(BolinkIncomeTbExample example);

    BolinkIncomeTb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BolinkIncomeTb record, @Param("example") BolinkIncomeTbExample example);

    int updateByExample(@Param("record") BolinkIncomeTb record, @Param("example") BolinkIncomeTbExample example);

    int updateByPrimaryKeySelective(BolinkIncomeTb record);

    int updateByPrimaryKey(BolinkIncomeTb record);

    int getIncomeCounts(BolinkIncomeTbExample example);

    List<Map<String,Object>> getIncomes(BolinkIncomeTbExample example);

    BigDecimal getIncomesMoney(BolinkIncomeTbExample example);
}