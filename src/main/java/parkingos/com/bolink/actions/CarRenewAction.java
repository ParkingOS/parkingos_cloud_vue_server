package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import parkingos.com.bolink.service.CarRenewService;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping("/carrenew")
public class CarRenewAction {

	Logger logger = Logger.getLogger(CarRenewAction.class);


	@Autowired
	private CarRenewService carRenewService;

	@RequestMapping(value = "/query")
	public String query(HttpServletRequest req, HttpServletResponse resp) {

		Map<String, String[]> reqParameterMap = req.getParameterMap();

		JSONObject result = carRenewService.selectResultByConditions(reqParameterMap);

		logger.info(result);
		StringUtils.ajaxOutput(resp,result.toJSONString());
		return null;
	}
}