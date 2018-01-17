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
		cardRenewTb.setComid(reqmap.get("comid"));
		JSONObject result = supperSearchService.supperSearch(cardRenewTb,reqmap);

		return result;
	}

}
