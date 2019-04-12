package parkingos.com.bolink.dao.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parkingos.com.bolink.dao.mybatis.BolinkExpenseTbExample;
import parkingos.com.bolink.dao.mybatis.BolinkIncomeTbExample;
import parkingos.com.bolink.models.BolinkExpenseTb;
@Mapper
public interface BolinkExpenseTbMapper {
    int countByExample(BolinkExpenseTbExample example);

    int deleteByExample(BolinkExpenseTbExample example);

    int deleteByPrimaryKey(Long id);

    int insert(BolinkExpenseTb record);

    int insertSelective(BolinkExpenseTb record);

    List<BolinkExpenseTb> selectByExample(BolinkExpenseTbExample example);

    BolinkExpenseTb selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BolinkExpenseTb record, @Param("example") BolinkExpenseTbExample example);

    int updateByExample(@Param("record") BolinkExpenseTb record, @Param("example") BolinkExpenseTbExample example);

    int updateByPrimaryKeySelective(BolinkExpenseTb record);

    int updateByPrimaryKey(BolinkExpenseTb record);


    int getExpenseCounts(BolinkExpenseTbExample example);

    List<Map<String,Object>> getExpenses(BolinkExpenseTbExample example);

    BigDecimal getExpensesMoney(BolinkExpenseTbExample example);
}