package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.EquipmentManageCameraService;
import parkingos.com.bolink.service.EquipmentManageLEDService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_camera")
public class EquipmentManageCameraAction {

	Logger logger = Logger.getLogger(EquipmentManageCameraAction.class);


	@Autowired
	private EquipmentManageCameraService equipmentManageCameraService;

	@RequestMapping(value = "/query")
	public String query(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);
		//request中包含6个主要参数。fieldsstr(表单)、rp()、token()、page()、orderby()、orderfield()

		logger.info(reqParameterMap);
		System.out.println(reqParameterMap);
		JSONObject result = equipmentManageCameraService.selectResultByConditions(reqParameterMap);



		logger.info(result);
		StringUtils.ajaxOutput(response,result.toJSONString());
		return null;
	}
}