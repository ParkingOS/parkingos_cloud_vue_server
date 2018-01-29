package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComLedTb;
import parkingos.com.bolink.service.EquipmentManageLEDService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_LED")
public class EquipmentManageLEDAction {

	Logger logger = Logger.getLogger(EquipmentManageLEDAction.class);


	@Autowired
	private EquipmentManageLEDService equipmentManageLEDService;
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

		JSONObject result = equipmentManageLEDService.selectResultByConditions(reqParameterMap);



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
	public String update(HttpServletRequest request, HttpServletResponse response) {

		String ledip = RequestUtil.processParams(request,"ledip");
		String ledport = RequestUtil.processParams(request,"ledport");
		String leduid = RequestUtil.processParams(request,"leduid");
		Integer movemode = RequestUtil.getInteger(request,"movemode",null);
		Integer movespeed = RequestUtil.getInteger(request,"movespeed",null);
		Long dwelltime = RequestUtil.getLong(request,"dwelltime",null);
		Integer ledcolor = RequestUtil.getInteger(request,"ledcolor",null);
		Integer showcolor = RequestUtil.getInteger(request,"showcolor",null);
		Integer typeface = RequestUtil.getInteger(request,"typeface",null);
		Integer typefont = RequestUtil.getInteger(request,"typesize",null);
		String matercont = RequestUtil.processParams(request,"matercont");
		Integer width = RequestUtil.getInteger(request,"width",128);
		Integer height = RequestUtil.getInteger(request,"height",32);
		Integer type = RequestUtil.getInteger(request,"type",0);
		Integer rsport = RequestUtil.getInteger(request,"rsport",1);
		Long passid = RequestUtil.getLong(request,"passid",null);

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		Long comid = Long.valueOf(Integer.valueOf(reqParameterMap.get("comid")));


		ComLedTb comLedTb = new ComLedTb();
		comLedTb.setLedip(ledip);
		comLedTb.setLedport(ledport);
		comLedTb.setLeduid(leduid);
		comLedTb.setMovemode(movemode);
		comLedTb.setMovespeed(movespeed);
		comLedTb.setDwelltime(dwelltime);
		comLedTb.setLedcolor(ledcolor);
		comLedTb.setShowcolor(showcolor);
		comLedTb.setTypeface(typeface);
		comLedTb.setTypesize(typefont);
		comLedTb.setMatercont(matercont);
		comLedTb.setWidth(width);
		comLedTb.setHeight(height);
		comLedTb.setType(type);
		comLedTb.setRsport(rsport);
		comLedTb.setState(-1);
		comLedTb.setComid(comid);
		comLedTb.setPassid(passid);

		String result = equipmentManageLEDService.insertResultByConditions(comLedTb).toString();
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
	public String edit(HttpServletRequest request, HttpServletResponse response) {

		Long id = RequestUtil.getLong(request,"id",null);
		String ledip = RequestUtil.processParams(request,"ledip");
		String ledport = RequestUtil.processParams(request,"ledport");
		String leduid = RequestUtil.processParams(request,"leduid");
		Integer movemode = RequestUtil.getInteger(request,"movemode",null);
		Integer movespeed = RequestUtil.getInteger(request,"movespeed",null);
		Long dwelltime = RequestUtil.getLong(request,"dwelltime",null);
		Integer ledcolor = RequestUtil.getInteger(request,"ledcolor",null);
		Integer showcolor = RequestUtil.getInteger(request,"showcolor",null);
		Integer typeface = RequestUtil.getInteger(request,"typeface",null);
		Integer typefont = RequestUtil.getInteger(request,"typesize",null);
		String matercont = RequestUtil.processParams(request,"matercont");
		Integer width = RequestUtil.getInteger(request,"width",128);
		Integer height = RequestUtil.getInteger(request,"height",32);
		Integer type = RequestUtil.getInteger(request,"type",0);
		Integer rsport = RequestUtil.getInteger(request,"rsport",1);
		Long passid = RequestUtil.getLong(request,"passid",null);

		ComLedTb comLedTb = new ComLedTb();
		comLedTb.setId(id);
		comLedTb.setLedip(ledip);
		comLedTb.setLedport(ledport);
		comLedTb.setLeduid(leduid);
		comLedTb.setMovemode(movemode);
		comLedTb.setMovespeed(movespeed);
		comLedTb.setDwelltime(dwelltime);
		comLedTb.setLedcolor(ledcolor);
		comLedTb.setShowcolor(showcolor);
		comLedTb.setTypeface(typeface);
		comLedTb.setTypesize(typefont);
		comLedTb.setMatercont(matercont);
		comLedTb.setWidth(width);
		comLedTb.setHeight(height);
		comLedTb.setType(type);
		comLedTb.setRsport(rsport);
		comLedTb.setPassid(passid);

		String result = equipmentManageLEDService.updateResultByConditions(comLedTb).toString();
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

		ComLedTb comLedTb = new ComLedTb();
		comLedTb.setId(id);
		comLedTb.setState(1);

		String result = equipmentManageLEDService.removeResultByConditions(comLedTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}