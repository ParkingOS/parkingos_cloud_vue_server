package parkingos.com.bolink.actions;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.service.LoginService;
import parkingos.com.bolink.utils.Encryption;
import parkingos.com.bolink.utils.RequestUtil;
import parkingos.com.bolink.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/user")
public class LoginAction {

	Logger logger = LoggerFactory.getLogger(LoginAction.class);
	@Autowired
	private CommonDao commonDao;
	@Autowired
	private LoginService loginService;

	@RequestMapping(value = "/dologin")
	public String dologin(HttpServletRequest req, HttpServletResponse resp) {


		String userId = req.getParameter("username");
		String cpasswd = req.getParameter("password");
	    String passwd = Encryption.decryptToAESPKCS5(cpasswd, Encryption.KEY);

		//根据用户名查询用户返回result
		JSONObject result = loginService.getResultByUserNameAndPass(userId,passwd);



//		logger.info(">>>>>>>用户 " + userId + " 正在登录系统!");
//		Long uid = -1L;
//		result.put("state",true);
//		result.put("token","xuluxuluxluxewrwerwe");
//		JSONObject user = JSONObject.parseObject("{}");
//		user.put("comid",1222);
//		user.put("nickname","liu");
//		user.put("roleid",30);
//		user.put("userid",userId);
//		user.put("lastlogin",System.currentTimeMillis()/1000);
//		user.put("parkid",21879);
//		result.put("user",user);

		StringUtils.ajaxOutput(resp,result.toJSONString());
		return null;
	}

	@RequestMapping(value="/getckey",method= RequestMethod.POST)
	public String getAESCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String mobile = RequestUtil.getString(request, "mobile");
		String userId = RequestUtil.getString(request, "userid");
		Integer userType = RequestUtil.getInteger(request, "user_type", 4);

		JSONObject result = loginService.getCkey(userId,mobile,userType);

		StringUtils.ajaxOutput(response, result.toJSONString());
		return null;
	}


	@RequestMapping(value="/reguser",method=RequestMethod.POST)
	public String regUser(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> retMap = new HashMap<String, Object>();
		//获取ckey,验证
		String mobile = RequestUtil.getString(request, "mobile");
		Long userid = RequestUtil.getLong(request, "userid", -1L);

		JSONObject result = loginService.regUser(mobile,userid);

		StringUtils.ajaxOutput(response, result.toJSONString());
		return null;
	}


	@RequestMapping(value="/checkcode",method=RequestMethod.POST)
	public String checkCode(HttpServletRequest request, HttpServletResponse response){
		//获取验证码,对比缓存中,对比数据库中,正确返回1,错误返回0
		Map<String, Object> retMap = new HashMap<String, Object>();
		String mobile = RequestUtil.getString(request, "mobile");
		String code = RequestUtil.getString(request, "code");

		JSONObject result = loginService.checkCode(mobile,code);

		StringUtils.ajaxOutput(response, result.toJSONString());
		return null;
	}


	@RequestMapping(value="/resetpwd",method=RequestMethod.POST)
	public String resetPwd(HttpServletRequest request, HttpServletResponse response){
		//获取密码,存入数据库
		Map<String, Object> retMap = new HashMap<String, Object>();
		String passwd = RequestUtil.getString(request, "passwd");
//		String dbpwd = StringUtils.MD5(passwd+Encryption.KEY);

		Long userId = RequestUtil.getLong(request, "user_id",-1L);

		logger.info("重置密码~~~"+passwd+"~~~"+userId);
		JSONObject result = loginService.resetPwd(passwd,userId);

		StringUtils.ajaxOutput(response, result.toJSONString());
		return null;
	}

}