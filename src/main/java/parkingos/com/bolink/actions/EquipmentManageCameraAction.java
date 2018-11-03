package parkingos.com.bolink.actions;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.models.ComCameraTb;
import parkingos.com.bolink.service.EquipmentManageCameraService;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/EQ_camera")
public class EquipmentManageCameraAction {

	Logger logger = LoggerFactory.getLogger(EquipmentManageCameraAction.class);


	@Autowired
	private EquipmentManageCameraService equipmentManageCameraService;

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/query")
	public String query(HttpServletRequest request, HttpServletResponse response) {

		Map<String, String> reqParameterMap = RequestUtil.readBodyFormRequset(request);

		JSONObject result = equipmentManageCameraService.selectResultByConditions(reqParameterMap);

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
		String cameraName = RequestUtil.processParams(request,"camera_name");
		String ip = RequestUtil.processParams(request,"ip");
		String port = RequestUtil.processParams(request,"port");
		String cusername = RequestUtil.processParams(request,"cusername");
		String manufacturer = RequestUtil.processParams(request,"manufacturer");
		Long passid = RequestUtil.getLong(request,"passid",-1l);
		Map<String,String> reqParamterMap = RequestUtil.readBodyFormRequset(request);
		Long comid = Long.valueOf(Integer.valueOf(reqParamterMap.get("comid")));

		ComCameraTb comCameraTb = new ComCameraTb();
		comCameraTb.setId(id);
		comCameraTb.setCameraName(cameraName);
		comCameraTb.setIp(ip);
		comCameraTb.setPort(port);
		comCameraTb.setCusername(cusername);
		comCameraTb.setManufacturer(manufacturer);
		comCameraTb.setPassid(passid);
		comCameraTb.setState(1);
		comCameraTb.setComid(comid);


		String result = equipmentManageCameraService.insertResultByConditions(comCameraTb).toString();

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
		String cameraName = RequestUtil.processParams(request,"camera_name");
		String ip = RequestUtil.processParams(request,"ip");
		String port = RequestUtil.processParams(request,"port");
		String cusername = RequestUtil.processParams(request,"cusername");
		String manufacturer = RequestUtil.processParams(request,"manufacturer");
		Long passid = RequestUtil.getLong(request,"passid",-1l);

		ComCameraTb comCameraTb = new ComCameraTb();
		comCameraTb.setId(id);
		comCameraTb.setCameraName(cameraName);
		comCameraTb.setIp(ip);
		comCameraTb.setPort(port);
		comCameraTb.setCusername(cusername);
		comCameraTb.setManufacturer(manufacturer);
		comCameraTb.setPassid(passid);


		String result = equipmentManageCameraService.updateResultByConditions(comCameraTb).toString();

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
		//Integer state = RequestUtil.getInteger(request,"state",null);

		ComCameraTb comCameraTb = new ComCameraTb();
		comCameraTb.setId(id);
		comCameraTb.setState(0);

		String result = equipmentManageCameraService.removeResultByConditions(comCameraTb).toString();

		StringUtils.ajaxOutput(response,result);
		return null;
	}
}