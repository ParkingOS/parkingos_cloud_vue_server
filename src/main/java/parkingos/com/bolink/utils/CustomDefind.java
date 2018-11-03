package parkingos.com.bolink.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取配置文件
 * @author Administrator
 *
 */
@ConfigurationProperties
public class CustomDefind {

	private static Map<String, String> config_map = new HashMap<String, String>();
	private static Logger logger = LoggerFactory.getLogger(CustomDefind.class);
	//private static String PATH = ;

	static {
		load();
	}

	public static String MESSAGESIGN = getValue("MESSAGESIGN");
	public static String CUSTOMPARKIDS = getValue("CUSTOMPARKIDS");
	public static String ISLOTTERY = getValue("ISLOTTERY");
	public static String MONGOADDRESS = getValue("MONGOADDRESS");
	public static String IMAGEURL = getValue("IMAGEURL");
	public static String SENDTICKET = getValue("SENDTICKET");
	public static String PARKBACK = getValue("PARKBACK");
	public static String ETCPARK = getValue("ETCPARK");
	public static String LOCALMAXVERSION = getValue("LOCALMAXVERSION");
	public static String TASKTYPE = getValue("TASKTYPE");
	public static String SECRETPARK = getValue("SECRETPARK");

	public static String UNIONIP = CustomDefind.getValue("UNIONIP");//泊链平台地址
	public static String UNIONID = CustomDefind.getValue("UNIONID");//泊链平台账户
	public static String SERVERID = CustomDefind.getValue("SERVERID");//泊链平台服务商号
	public static String UNIONKEY = CustomDefind.getValue("UNIONKEY");//泊链平台身份密钥
	public static String USERUPMONEY = CustomDefind.getValue("USERUPMONEY");//车主在泊链平台的限额
	public static String UNIONVALUE = CustomDefind.getValue("UNIONVALUE");//泊链平台英文简称

	//添加是否支持判断ETCPARK的字段判定值
	public static String ISSUPPORTETCPARK = CustomDefind.getValue("ISSUPPORTETCPARK");

	public static void load(){
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		Properties p = new Properties();
		try {
			p.load(is);
			for (Map.Entry<Object,Object> e : p.entrySet()) {
				config_map.put((String) e.getKey(), (String) e.getValue());
			}
			logger.info("===>>>>init:"+config_map);
		} catch (IOException e) {
			logger.error("load property file failed", e);
		}
	}

	public static String getValue(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return config_map.get(key);
	}


}
