package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PrepayCardDeductedTb;
import parkingos.com.bolink.models.PrepayCardTrade;
import parkingos.com.bolink.service.PrepayCardUseTradeService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;

import java.util.List;
import java.util.Map;


@Service
public class PrepayCardUseTradeServiceImpl implements PrepayCardUseTradeService {

	Logger logger = LoggerFactory.getLogger(PrepayCardUseTradeServiceImpl.class);

	@Autowired
	private CommonDao commonDao;
	@Autowired
    private SupperSearchService<PrepayCardDeductedTb> supperSearchService;

	@Override
	public JSONObject selectResultByConditions(Map<String,String> reqmap) {

		PrepayCardDeductedTb prepayCardTrade = new PrepayCardDeductedTb();
		prepayCardTrade.setParkId(Long.parseLong(reqmap.get("comid")));

		JSONObject result = supperSearchService.supperSearch(prepayCardTrade,reqmap);

		//获得所有的数据得到 所有应收实收  不需要分页  查询所有  没有这两个属性后面就没有分页
		reqmap.remove("orderfield");
		reqmap.remove("orderby");

		JSONObject newResult = supperSearchService.supperSearch(prepayCardTrade,reqmap);
		List<PrepayCardTrade> cardRenewList = JSON.parseArray(newResult.get("rows").toString(), PrepayCardTrade.class);
		Double amountReceivable  = 0.0;
		if(cardRenewList!=null&&cardRenewList.size()>0) {
			for (PrepayCardTrade trade : cardRenewList) {
				try {
					amountReceivable += Double.parseDouble(trade.getAddMoney()+"");
				}catch (Exception e){
					amountReceivable += 0;
				}

			}
		}
		result.put("amountReceivable",StringUtils.formatDouble(amountReceivable));

		return result;
	}

}
