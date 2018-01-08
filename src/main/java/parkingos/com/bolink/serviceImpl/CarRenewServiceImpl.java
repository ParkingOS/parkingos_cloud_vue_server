package parkingos.com.bolink.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zld.common_dao.dao.CommonDao;
import com.zld.common_dao.qo.PageOrderConfig;
import com.zld.common_dao.util.OrmUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.models.CardRenewTb;
import parkingos.com.bolink.service.CarRenewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class CarRenewServiceImpl implements CarRenewService {

	Logger logger = Logger.getLogger(CarRenewServiceImpl.class);

	@Autowired
	private CommonDao commonDao;

	@Override
	public int selectCountByConditions(CardRenewTb cardRenewTb) {
		return commonDao.selectCountByConditions(cardRenewTb);
	}

	@Override
	public JSONObject selectResultByConditions(Map<String,String[]> reqmap) {


		//测试期间设置登录有效期为1小时
		String str = "{\"total\":12,\"page\":1,\"money\":\"应收 222.03元，实收 41.23元\",\"rows\":[]}";
		JSONObject result = JSONObject.parseObject(str);

		CardRenewTb cardRenewTb  = new CardRenewTb();
		cardRenewTb.setComid("21782");
		int count = commonDao.selectCountByConditions(cardRenewTb);

		if(count>0) {
			/**分页处理*/
			PageOrderConfig config = new PageOrderConfig();
			config.setPageInfo(Integer.parseInt(reqmap.get("page")[0]), Integer.parseInt(reqmap.get("rp")[0]));

			List<CardRenewTb> list = commonDao.selectListByConditions(cardRenewTb, config);
			List<Map<String, Object>> resList = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (CardRenewTb renewTb : list) {
					OrmUtil<CardRenewTb> otm = new OrmUtil<>();
					Map<String, Object> map = otm.pojoToMap(renewTb);
					resList.add(map);
				}
				result.put("rows", JSON.toJSON(resList));
			}
		}
		result.put("total", count);
		result.put("page", Integer.parseInt(reqmap.get("page")[0]));
		return result;
	}

}
