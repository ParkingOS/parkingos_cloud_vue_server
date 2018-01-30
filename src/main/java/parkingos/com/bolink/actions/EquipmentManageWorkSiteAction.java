package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComWorksiteTb;
import parkingos.com.bolink.service.EquipmentManageWorkSiteService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_workStation")
public class EquipmentManageWorkSiteAction {

	Logger logger = Logger.getLogger(EquipmentManageWorkSiteAction.class);


	@Autowired
	private EquipmentManageWorkSiteService equipmentManageWorkSiteService;
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

		JSONObject result = equipmentManageWorkSiteService.selectResultByConditions(reqParameterMap);

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

		String worksiteName = RequestUtil.processParams(request,"worksite_name");
		String description = RequestUtil.processParams(request,"description");
		Integer netType = RequestUtil.getInteger(request,"net_type",0);
		Long id = RequestUtil.getLong(request,"id",-1L);
		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		Long comid = Long.valueOf(Integer.valueOf(reqParameterMap.get("comid")));

		ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
		comWorksiteTb.setWorksiteName(worksiteName);
		comWorksiteTb.setDescription(description);
		comWorksiteTb.setNetType(netType);
		comWorksiteTb.setId(id);
		comWorksiteTb.setComid(comid);
		comWorksiteTb.setState(0);

		String result = equipmentManageWorkSiteService.insertResultByConditions(comWorksiteTb).toString();

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
		Long comid = RequestUtil.getLong(request,"comid",-1L);
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

		ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
		comWorksiteTb.setId(id);
		comWorksiteTb.setState(1);

		String result = equipmentManageWorkSiteService.removeResultByConditions(comWorksiteTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}