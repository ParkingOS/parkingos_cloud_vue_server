package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CardRenewTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.CarRenewService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class CarRenewServiceImpl implements CarRenewService {

	Logger logger = Logger.getLogger(CarRenewServiceImpl.class);

	@Autowired
	private CommonDao commonDao;
	@Autowired
    private SupperSearchService<CardRenewTb> supperSearchService;
	@Override
	public int selectCountByConditions(CardRenewTb cardRenewTb) {
		return commonDao.selectCountByConditions(cardRenewTb);
	}

	@Override
	public JSONObject selectResultByConditions(Map<String,String> reqmap) {

		logger.info(reqmap);
		//测试期间设置登录有效期为1小时
		CardRenewTb cardRenewTb = new CardRenewTb();
		cardRenewTb.setComid(reqmap.get("comid"));
		JSONObject result = supperSearchService.supperSearch(cardRenewTb,reqmap);


		//获得所有的数据得到 所有应收实收  不需要分页  查询所有  没有这两个属性后面就没有分页
		reqmap.remove("orderfield");
		reqmap.remove("orderby");

		JSONObject newResult = supperSearchService.supperSearch(cardRenewTb,reqmap);
		List<CardRenewTb> cardRenewList = JSON.parseArray(newResult.get("rows").toString(), CardRenewTb.class);
		logger.error("======>>>>.月卡续费"+cardRenewList.size());
		Double amountReceivable  = 0.0;
		Double actReceivable = 0.0;
		if(cardRenewList!=null&&cardRenewList.size()>0) {
			for (CardRenewTb cardRenew : cardRenewList) {
				amountReceivable += Double.parseDouble(cardRenew.getAmountReceivable());
				actReceivable += Double.parseDouble(cardRenew.getAmountPay());
			}
		}
		result.put("amountReceivable",StringUtils.formatDouble(amountReceivable));
		result.put("actReceivable",StringUtils.formatDouble(actReceivable));

		return result;
	}


	private String getUinname(Long uin) {
		UserInfoTb userInfoTb = new UserInfoTb();
		userInfoTb.setId(uin);
		userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);
		if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
			return userInfoTb.getNickname();
		}
		return "";
	}

	@Override
	public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

		//删除分页条件  查询该条件下所有  不然为一页数据
		reqParameterMap.remove("orderfield");
		reqParameterMap.remove("orderby");

		//获取要到处的数据
		JSONObject result = selectResultByConditions(reqParameterMap);
		List<CardRenewTb> cardRenewList = JSON.parseArray(result.get("rows").toString(), CardRenewTb.class);

		logger.error("=========>>>>>>.导出月卡续费" + cardRenewList.size());

		List<List<Object>> bodyList = new ArrayList<List<Object>>();
		if (cardRenewList != null && cardRenewList.size() > 0) {
			String[] f = new String[]{"id", "trade_no", "card_id", "pay_time", "amount_receivable", "amount_pay", "collector", "pay_type", "car_number", "user_id", "limit_time", "resume"};
			for (CardRenewTb cardRenewTb : cardRenewList) {
				//javabean转map取参数
//				List<String> values = new ArrayList<String>();
				List<Object> values = new ArrayList<Object>();
				OrmUtil<CardRenewTb> otm = new OrmUtil<>();
				Map map = otm.pojoToMap(cardRenewTb);
				//判断各种字段 组装导出数据
				for (String field : f) {
					if ("collector".equals(field)) {
						Object uid = map.get("collector");
						if (Check.isNumber(uid + ""))
							if (getUinname(Long.valueOf(map.get(field) + "")) != null) {
								values.add(getUinname(Long.valueOf(map.get(field) + "")));
							} else {
								values.add(uid + "");
							}
						else
							values.add(uid + "");
					} else {
						if ("create_time".equals(field) || "pay_time".equals(field) || "limit_time".equals(field)) {
							if (map.get(field) != null) {
								values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((map.get(field) + "")) * 1000));
							} else {
								values.add("null");
							}
						} else {
							values.add(map.get(field) + "");
						}
					}
				}
				bodyList.add(values);
			}
		}
		return bodyList;
	}

}
