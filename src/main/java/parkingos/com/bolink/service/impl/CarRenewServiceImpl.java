package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CardRenewTb;
import parkingos.com.bolink.service.CarRenewService;
import parkingos.com.bolink.service.SupperSearchService;

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
		cardRenewTb.setComid("21782");
		JSONObject result = supperSearchService.supperSearch(cardRenewTb,reqmap);
		/*String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
		JSONObject result = JSONObject.parseObject(str);
		int count =0;
		List<CardRenewTb> list =null;
		List<Map<String, Object>> resList =new ArrayList<>();
		Map searchMap = supperSearchService.getBaseSearch(new CardRenewTb(),reqmap);
		logger.info(searchMap);
		if(searchMap!=null&&!searchMap.isEmpty()){
			CardRenewTb baseQuery =(CardRenewTb)searchMap.get("base");
			List<SearchBean> supperQuery = null;
			if(searchMap.containsKey("supper"))
				supperQuery = (List<SearchBean>)searchMap.get("supper");
			PageOrderConfig config = null;
			if(searchMap.containsKey("config"))
				config = (PageOrderConfig)searchMap.get("config");
			count = commonDao.selectCountByConditions(baseQuery,supperQuery);
			if(count>0){
				list = commonDao.selectListByConditions(baseQuery,supperQuery,config);

				if (list != null && !list.isEmpty()) {
					for (CardRenewTb renewTb : list) {
						OrmUtil<CardRenewTb> otm = new OrmUtil<>();
						Map<String, Object> map = otm.pojoToMap(renewTb);
						resList.add(map);
					}
					result.put("rows", JSON.toJSON(resList));
				}
			}
		}
		result.put("total", count);
		result.put("page", Integer.parseInt(reqmap.get("page")));
		return result;*/
		return result;
	}

}
