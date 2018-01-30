package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.MonitorInfoTb;
import parkingos.com.bolink.service.EquipmentManageMonitorService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_monitor")
public class EquipmentManageMonitorAction {

	Logger logger = Logger.getLogger(EquipmentManageMonitorAction.class);


	@Autowired
	private EquipmentManageMonitorService equipmentManageMonitorService;
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

		JSONObject result = equipmentManageMonitorService.selectResultByConditions(reqParameterMap);

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

		//Long id = RequestUtil.getLong(request,"id",null);
		String name = RequestUtil.processParams(request,"name");
		Long channelId = RequestUtil.getLong(request,"channel_id",null);
		Integer netStatus = RequestUtil.getInteger(request,"net_status",null);
		Integer isShow = RequestUtil.getInteger(request,"is_show",1);
		Integer showOrder = RequestUtil.getInteger(request,"show_order",null);
		String playSrc = RequestUtil.processParams(request,"play_src");

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		String comid = reqParameterMap.get("comid");

		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		//monitorInfoTb.setId(id);
		monitorInfoTb.setName(name);
		monitorInfoTb.setChannelId(channelId);
		monitorInfoTb.setNetStatus(netStatus);
		monitorInfoTb.setIsShow(isShow);
		monitorInfoTb.setShowOrder(showOrder);
		monitorInfoTb.setPlaySrc(playSrc);
		monitorInfoTb.setComid(comid);
		monitorInfoTb.setState(1);

		String result = equipmentManageMonitorService.insertResultByConditions(monitorInfoTb).toString();

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

		Long id = RequestUtil.getLong(request,"id",null);
		Long name = RequestUtil.getLong(request,"name",null);
		Long monitorId = RequestUtil.getLong(request,"monitor_id",null);
		Long channelId = RequestUtil.getLong(request,"channel_id",null);
		Integer netStatus = RequestUtil.getInteger(request,"net_status",null);
		Integer isShow = RequestUtil.getInteger(request,"is_show",1);
		Integer showOrder = RequestUtil.getInteger(request,"show_order",null);
		String playSrc = RequestUtil.processParams(request,"play_src");

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		String comid = reqParameterMap.get("comid").toString();

		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		monitorInfoTb.setId(id);
		//monitorInfoTb.setMonitorId(monitorId);
		monitorInfoTb.setChannelId(channelId);
		monitorInfoTb.setNetStatus(netStatus);
		monitorInfoTb.setIsShow(isShow);
		monitorInfoTb.setShowOrder(showOrder);
		monitorInfoTb.setPlaySrc(playSrc);
		monitorInfoTb.setComid(comid);

		String result = equipmentManageMonitorService.updateResultByConditions(monitorInfoTb).toString();
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

		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		monitorInfoTb.setId(id);
		monitorInfoTb.setState(0);

		String result = equipmentManageMonitorService.removeResultByConditions(monitorInfoTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}