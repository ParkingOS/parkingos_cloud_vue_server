package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PrepayCardTrade;
import parkingos.com.bolink.service.PrepayCardTradeService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;

import java.util.List;
import java.util.Map;


@Service
public class PrepayCardTradeServiceImpl implements PrepayCardTradeService {

	Logger logger = LoggerFactory.getLogger(PrepayCardTradeServiceImpl.class);

	@Autowired
	private CommonDao commonDao;
	@Autowired
    private SupperSearchService<PrepayCardTrade> supperSearchService;

	@Override
	public JSONObject selectResultByConditions(Map<String,String> reqmap) {

		//测试期间设置登录有效期为1小时
		PrepayCardTrade prepayCardTrade = new PrepayCardTrade();
		prepayCardTrade.setParkId(Long.parseLong(reqmap.get("comid")));
		prepayCardTrade.setState(1);

		String time_type = reqmap.get("time_type");
		Integer timeType = 3;
		if(Check.isNumber(time_type)){
			timeType = Integer.parseInt(time_type);
		}
		if(reqmap.get("ctime")!=null) {
			//开始时间
			if (timeType == 1) {
				reqmap.put("start_time", reqmap.get("ctime"));
				reqmap.put("start_time_start", reqmap.get("ctime_start"));
				reqmap.put("start_time_end", reqmap.get("ctime_end"));
			}
			//结束时间
			if (timeType == 2) {
				reqmap.put("limit_time", reqmap.get("ctime"));
				reqmap.put("limit_time_start", reqmap.get("ctime_start"));
				reqmap.put("limit_time_end", reqmap.get("ctime_end"));
			}

			if (timeType == 3) {
				reqmap.put("pay_time", reqmap.get("ctime"));
				reqmap.put("pay_time_start", reqmap.get("ctime_start"));
				reqmap.put("pay_time_end", reqmap.get("ctime_end"));
			}
		}

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
