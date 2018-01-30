package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComPassTb;
import parkingos.com.bolink.service.EquipmentManageChannelService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_channel")
public class EquipmentManageChannelAction {

	Logger logger = Logger.getLogger(EquipmentManageChannelAction.class);


	@Autowired
	private EquipmentManageChannelService equipmentManageChannelService;
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

		JSONObject result = equipmentManageChannelService.selectResultByConditions(reqParameterMap);



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

		Long id = RequestUtil.getLong(request,"id",null);
		String passname = RequestUtil.processParams(request,"passname");
		String passtype = RequestUtil.processParams(request,"passtype");
		Integer monthSet = RequestUtil.getInteger(request,"month_set",0);
		Integer month2Set = RequestUtil.getInteger(request,"month2_set",0);
		Long worksiteId = RequestUtil.getLong(request,"worksite_id",-1L);
		String description = RequestUtil.processParams(request,"description");

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		Long comid = Long.valueOf(Integer.valueOf(reqParameterMap.get("comid")));

		ComPassTb comPassTb = new ComPassTb();
		comPassTb.setId(id);
		comPassTb.setPassname(passname);
		comPassTb.setPasstype(passtype);
		comPassTb.setMonthSet(monthSet);
		comPassTb.setMonth2Set(month2Set);
		comPassTb.setWorksiteId(worksiteId);
		comPassTb.setDescription(description);
		comPassTb.setComid(comid);
		comPassTb.setState(0);

		String result = equipmentManageChannelService.insertResultByConditions(comPassTb).toString();

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
		String passname = RequestUtil.processParams(request,"passname");
		String passtype = RequestUtil.processParams(request,"passtype");
		Integer monthSet = RequestUtil.getInteger(request,"month_set",0);
		Integer month2Set = RequestUtil.getInteger(request,"month2_set",0);
		Long worksiteId = RequestUtil.getLong(request,"worksite_id",-1L);
		String description = RequestUtil.processParams(request,"description");

		ComPassTb comPassTb = new ComPassTb();
		comPassTb.setId(id);
		comPassTb.setPassname(passname);
		comPassTb.setPasstype(passtype);
		comPassTb.setMonthSet(monthSet);
		comPassTb.setMonth2Set(month2Set);
		comPassTb.setWorksiteId(worksiteId);
		comPassTb.setDescription(description);

		String result = equipmentManageChannelService.updateResultByConditions(comPassTb).toString();

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
		Long id = RequestUtil.getLong(request,"id",null);

		ComPassTb comPassTb = new ComPassTb();
		comPassTb.setId(id);
		comPassTb.setState(1);

		String result = equipmentManageChannelService.removeResultByConditions(comPassTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}