package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComWorksiteTb;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.EquipmentManageWorkSiteService;
import parkingos.com.bolink.service.SaveLogService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_workStation")
public class EquipmentManageWorkSiteAction {

	Logger logger = LoggerFactory.getLogger(EquipmentManageWorkSiteAction.class);


	@Autowired
	private EquipmentManageWorkSiteService equipmentManageWorkSiteService;
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


		JSONObject result = equipmentManageWorkSiteService.selectResultByConditions(reqParameterMap);

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

		String worksiteName = RequestUtil.processParams(request,"worksite_name");
		String description = RequestUtil.processParams(request,"description");
		Integer netType = RequestUtil.getInteger(request,"net_type",0);
		Long id = equipmentManageWorkSiteService.getId();
		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
		comWorksiteTb.setWorksiteName(worksiteName);
		comWorksiteTb.setDescription(description);
		comWorksiteTb.setNetType(netType);
		comWorksiteTb.setId(id);
		comWorksiteTb.setComid(comid);
		comWorksiteTb.setState(0);

		String result = equipmentManageWorkSiteService.insertResultByConditions(comWorksiteTb).toString();

		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(1);
			parkLogTb.setContent(uin+"("+nickname+")"+"增加了工作站"+worksiteName);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
		}

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

		Long comid = RequestUtil.getLong(request,"comid",-1L);
		String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
		Long uin = RequestUtil.getLong(request, "loginuin", -1L);

		Long id = RequestUtil.getLong(request,"id",null);
		String worksiteName = RequestUtil.processParams(request,"worksite_name");
		String description = RequestUtil.processParams(request,"description");
		Integer netType = RequestUtil.getInteger(request,"net_type",0);

		ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
		comWorksiteTb.setId(id);
		comWorksiteTb.setComid(comid);
		comWorksiteTb.setWorksiteName(worksiteName);
		comWorksiteTb.setDescription(description);
		comWorksiteTb.setNetType(netType);

		String result = equipmentManageWorkSiteService.updateResultByConditions(comWorksiteTb).toString();

		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(2);
			parkLogTb.setContent(uin+"("+nickname+")"+"修改了工作站"+id);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
		}

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
		Long comid = RequestUtil.getLong(request,"comid",-1L);
		String nickname = StringUtils.decodeUTF8(RequestUtil.getString(request,"nickname1"));
		Long uin = RequestUtil.getLong(request, "loginuin", -1L);

		Long id = RequestUtil.getLong(request,"id",null);

		ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
		comWorksiteTb.setId(id);
		comWorksiteTb.setState(1);

		String result = equipmentManageWorkSiteService.removeResultByConditions(comWorksiteTb).toString();

		if("1".equals(result)){
			ParkLogTb parkLogTb = new ParkLogTb();
			parkLogTb.setOperateUser(nickname);
			parkLogTb.setOperateTime(System.currentTimeMillis()/1000);
			parkLogTb.setOperateType(3);
			parkLogTb.setContent(uin+"("+nickname+")"+"删除了工作站"+id);
			parkLogTb.setType("equipment");
			parkLogTb.setParkId(comid);
			saveLogService.saveLog(parkLogTb);
		}

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}