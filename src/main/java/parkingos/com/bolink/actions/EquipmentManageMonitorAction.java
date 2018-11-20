package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.MonitorInfoTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.EquipmentManageMonitorService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_monitor")
public class EquipmentManageMonitorAction {

	Logger logger = LoggerFactory.getLogger(EquipmentManageMonitorAction.class);


	@Autowired
	private EquipmentManageMonitorService equipmentManageMonitorService;
	@Autowired
	private SaveLogService saveLogService;
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/query")
	public String query(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);


		JSONObject result = equipmentManageMonitorService.selectResultByConditions(reqParameterMap);

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

		Long comid = RequestUtil.getLong(request,"comid",-1L);
		String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
		Long uin = RequestUtil.getLong(request, "loginuin", -1L);

		Long id = equipmentManageMonitorService.getId();
		String name = RequestUtil.processParams(request,"name");
		Long channelId = RequestUtil.getLong(request,"channel_id",null);
		Integer netStatus = RequestUtil.getInteger(request,"net_status",null);
		Integer isShow = RequestUtil.getInteger(request,"is_show",1);
		Integer showOrder = RequestUtil.getInteger(request,"show_order",null);
		String playSrc = RequestUtil.processParams(request,"play_src");


		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		monitorInfoTb.setId(id);
		monitorInfoTb.setName(name);
		monitorInfoTb.setChannelId(channelId);
		monitorInfoTb.setNetStatus(netStatus);
		monitorInfoTb.setIsShow(isShow);
		monitorInfoTb.setShowOrder(showOrder);
		monitorInfoTb.setPlaySrc(playSrc);
		monitorInfoTb.setComid(comid+"");
		monitorInfoTb.setState(1);

		String result = equipmentManageMonitorService.insertResultByConditions(monitorInfoTb).toString();
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("state",0);
		jsonResult.put("msg","添加失败");
		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(1);
			parkLogTb.setContent(uin+"("+nickname+")"+"增加了监控"+id+name);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
			jsonResult.put("state",1);
			jsonResult.put("msg","添加成功");
		}

		StringUtils.ajaxOutput(response,jsonResult.toJSONString());

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

		Long comid = RequestUtil.getLong(request,"comid",-1L);
		String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
		Long uin = RequestUtil.getLong(request, "loginuin", -1L);

		Long id = RequestUtil.getLong(request,"id",null);
		String name = RequestUtil.getString(request,"name");
		//Long monitorId = RequestUtil.getLong(request,"monitor_id",null);
		Long channelId = RequestUtil.getLong(request,"channel_id",null);
		Integer netStatus = RequestUtil.getInteger(request,"net_status",null);
		Integer isShow = RequestUtil.getInteger(request,"is_show",1);
		Integer showOrder = RequestUtil.getInteger(request,"show_order",null);
		String playSrc = RequestUtil.processParams(request,"play_src");


		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		monitorInfoTb.setId(id);
		//monitorInfoTb.setMonitorId(monitorId);
		monitorInfoTb.setChannelId(channelId);
		monitorInfoTb.setName(name);
		monitorInfoTb.setNetStatus(netStatus);
		monitorInfoTb.setIsShow(isShow);
		monitorInfoTb.setShowOrder(showOrder);
		monitorInfoTb.setPlaySrc(playSrc);
		monitorInfoTb.setComid(comid+"");

		String result = equipmentManageMonitorService.updateResultByConditions(monitorInfoTb).toString();
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("state",0);
		jsonResult.put("msg","编辑失败");
		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(2);
			parkLogTb.setContent(uin+"("+nickname+")"+"修改了监控"+id);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
			jsonResult.put("state",1);
			jsonResult.put("msg","编辑成功");
		}

		StringUtils.ajaxOutput(response,jsonResult.toJSONString());

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

		Long comid = RequestUtil.getLong(request,"comid",-1L);
		String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
		Long uin = RequestUtil.getLong(request, "loginuin", -1L);

		Long id = RequestUtil.getLong(request,"id",-1l);

		MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
		monitorInfoTb.setId(id);
		monitorInfoTb.setState(0);

		String result = equipmentManageMonitorService.removeResultByConditions(monitorInfoTb).toString();
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("state",0);
		jsonResult.put("msg","删除失败");
		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(3);
			parkLogTb.setContent(uin+"("+nickname+")"+"删除了监控"+id);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
			jsonResult.put("state",1);
			jsonResult.put("msg","删除成功");
		}
		StringUtils.ajaxOutput(response,jsonResult.toJSONString());
		return null;
	}

	@RequestMapping("/groupmonitors")
	public String getGroupMonitors(HttpServletRequest request,HttpServletResponse response){

		System.out.println("=====进去获取集团所有监控方法");
//		Long groupid = RequestUtil.getLong(request,"groupid",-1L);
//		System.out.println("获得集团下面所有监控:"+groupid);
		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		JSONObject result = equipmentManageMonitorService.selectGroupMonitors(reqParameterMap);
		//把结果返回页面
		StringUtils.ajaxOutput(response,result.toJSONString());
		return null;

	}

}