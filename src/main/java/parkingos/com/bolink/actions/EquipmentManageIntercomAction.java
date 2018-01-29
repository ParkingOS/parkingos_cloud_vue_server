package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.PhoneInfoTb;
import parkingos.com.bolink.service.EquipmentManageIntercomService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_intercom")
public class EquipmentManageIntercomAction {

	Logger logger = Logger.getLogger(EquipmentManageIntercomAction.class);


	@Autowired
	private EquipmentManageIntercomService equipmentManageIntercomService;

	/**
	 * 查询监视器名称
	 * @param request
	 * @param response
	 * @return
	 */
	/*@RequestMapping(value = "/findMonitorName")
	public String findMonitor(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		logger.info(reqParameterMap);

		JSONObject result = equipmentManageIntercomService.findMonitorByConditions(reqParameterMap);

		logger.info(result);
		StringUtils.ajaxOutput(response,result.toJSONString());
		return null;
	}*/
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/query")
	public String query(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		logger.info(reqParameterMap);

		JSONObject result = equipmentManageIntercomService.selectResultByConditions(reqParameterMap);

		logger.info(result);
		StringUtils.ajaxOutput(response,result.toJSONString());
		return null;
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/add")
	public String add(HttpServletRequest request, HttpServletResponse response) {

		//Long id = RequestUtil.getLong(request,"id",-1l);
		String name = RequestUtil.processParams(request,"name");
		Long telePhone = RequestUtil.getLong(request,"tele_phone",null);
		Long parkPhone = RequestUtil.getLong(request,"park_phone",null);
		Long groupPhone = RequestUtil.getLong(request,"group_phone",null);
		Long monitorId = RequestUtil.getLong(request,"monitor_id",null);

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		String comid = reqParameterMap.get("comid");

		PhoneInfoTb phoneInfoTb = new PhoneInfoTb();
		//phoneInfoTb.setId(id);
		phoneInfoTb.setName(name);
		phoneInfoTb.setTelePhone(telePhone);
		phoneInfoTb.setParkPhone(parkPhone);
		phoneInfoTb.setGroupPhone(groupPhone);
		phoneInfoTb.setMonitorId(monitorId);
		phoneInfoTb.setComid(comid);
		phoneInfoTb.setState(1);
		String result = equipmentManageIntercomService.insertResultByConditions(phoneInfoTb).toString();

		StringUtils.ajaxOutput(response,result);

		return null;
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/edit")
	public String update(HttpServletRequest request, HttpServletResponse response) {
		Long id = RequestUtil.getLong(request,"id",-1l);
		String name = RequestUtil.processParams(request,"name");
		Long telePhone = RequestUtil.getLong(request,"tele_phone",null);
		Long parkPhone = RequestUtil.getLong(request,"park_phone",null);
		Long groupPhone = RequestUtil.getLong(request,"group_phone",null);
		Long monitorId = RequestUtil.getLong(request,"monitor_id",null);

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		PhoneInfoTb phoneInfoTb = new PhoneInfoTb();
		phoneInfoTb.setId(id);
		phoneInfoTb.setName(name);
		phoneInfoTb.setTelePhone(telePhone);
		phoneInfoTb.setParkPhone(parkPhone);
		phoneInfoTb.setGroupPhone(groupPhone);
		phoneInfoTb.setMonitorId(monitorId);

		String result = equipmentManageIntercomService.updateResultByConditions(phoneInfoTb).toString();
		StringUtils.ajaxOutput(response,result);

		return null;
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/remove")
	public String remove(HttpServletRequest request,HttpServletResponse response){
		Long id = RequestUtil.getLong(request,"id",-1l);

		PhoneInfoTb phoneInfoTb = new PhoneInfoTb();
		phoneInfoTb.setId(id);
		phoneInfoTb.setState(0);

		String result = equipmentManageIntercomService.removeResultByConditions(phoneInfoTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}