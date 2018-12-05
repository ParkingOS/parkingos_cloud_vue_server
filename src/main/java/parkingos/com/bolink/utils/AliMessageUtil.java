package parkingos.com.bolink.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * 阿里发送短信
 *
 */
public class AliMessageUtil {

    private static Logger logger = LoggerFactory.getLogger(AliMessageUtil.class);
    //初始化ascClient需要的几个参数
    private static String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
    private static String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
    //替换成你的AK
    private static String accessKeyId = "LTAIlJ4K2rOQZm5F";//你的accessKeyId,参考本文档步骤2
    private static String accessKeySecret = "KJuZruZkwmLuzv0phfI3nO0IIWVpt9";//你的accessKeySecret，参考本文档步骤2


    /*
    * 云平台短信验证码发送接口，签名写死的智慧停车云。
    * 模板写死的发送验证码的模板id
    * 变量名为code
    *
    *
    * */
    public static  String sendMessage(String phone,String code){
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phone);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("智慧停车云");
            //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode("SMS_151547232");
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            request.setTemplateParam("{\"code\":"+code+"}");
            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
            //request.setSmsUpExtendCode("90997");
            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//            request.setOutId("yourOutId");
            //请求失败这里会抛ClientException异常

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
                logger.info("send success!");
                return "1";
            }
        }catch (Exception e){
            logger.error("send message error",e);
        }
        return null;
    }




    public static Map sendVipNotice(Integer type, String carNumber, String date, Integer month, String mobile,String parkName) {

        Map<String,Object> map = new HashMap<>();
        map.put("state",0);
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            //初始化ascClient,暂时不支持多region（请勿修改）
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                    accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(mobile);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName("智慧停车云");
            /*
            * 1,新建月卡
            * 2,月卡修改车牌
            * 3,月卡续费
            * 4,月卡提前通知
            * */
            if(type==1){
                request.setTemplateCode("SMS_152213143");
                request.setTemplateParam("{\"car_number\":\""+carNumber+"\",\"date\":\""+date+"\",\"park_name\":\""+parkName+"\"}");
            }else if(type==2){
                logger.info("===>>>>>"+type);
                request.setTemplateCode("SMS_152208290");
                request.setTemplateParam("{\"car_number\":\""+carNumber+"\", \"date\":\""+date+"\",\"park_name\":\""+parkName+"\"}");
            }else if(type==3){
                request.setTemplateCode("SMS_152280406");
                request.setTemplateParam("{\"car_number\":\""+carNumber+"\",\"month\":\""+month+"\",\"date\":\""+date+"\",\"park_name\":\""+parkName+"\"}");
            }else if(type==4){
                request.setTemplateCode("SMS_152285408");
                request.setTemplateParam("{\"car_number\":\""+carNumber+"\",\"date\":\""+date+"\",\"day\":\""+month+"\",\"park_name\":\""+parkName+"\"}");
            }

            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            //返回biz_id   用来查询发送短信的状态。
            map.put("biz_id",sendSmsResponse.getBizId());
            logger.info("=====bizId:"+sendSmsResponse.getBizId());
            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功  有可能因为手机号原因发送失败
                logger.info("send success!");
                map.put("state",1);
            }
            map.put("errmsg",sendSmsResponse.getCode());
        }catch (Exception e){
            logger.error("send message error",e);
        }
        return map;
    }


    public static Map getSendState(String mobile,String bizId,String dateStr){
        Map<String,Object> map = new HashMap<>();
        try {
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            //云通信产品-短信API服务产品名称（短信产品名固定，无需修改）
            //初始化ascClient
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            QuerySendDetailsRequest request = new QuerySendDetailsRequest();
            //必填-号码
            request.setPhoneNumber(mobile);
            //可选-调用发送短信接口时返回的BizId
            request.setBizId(bizId);
            //必填-短信发送的日期 支持30天内记录查询（可查其中一天的发送数据），格式yyyyMMdd
            request.setSendDate(dateStr);
            //必填-页大小
            request.setPageSize(10L);
            //必填-当前页码从1开始计数
            request.setCurrentPage(1L);
            //hint 此处可能会抛出异常，注意catch
            QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);
            //获取返回结果
            map.put("state",0);
            if (querySendDetailsResponse.getCode() != null && querySendDetailsResponse.getCode().equals("OK")) {
                //代表请求成功
                /*
                *
                * sendStatus  1:等待回执
                *             2: 发送失败
                *             3:发送成功
                *
                * errCode   错误码
                * */
                QuerySendDetailsResponse.SmsSendDetailDTO detailDTO=querySendDetailsResponse.getSmsSendDetailDTOs().get(0);
                logger.info("==>>>:"+detailDTO.getSendStatus()+"~~"+detailDTO.getContent().length());
                logger.info("====>>>>>"+detailDTO.getContent());
                map.put("state",detailDTO.getSendStatus());
                map.put("content",detailDTO.getContent());
            }
        }catch (Exception e){
            logger.error("send error",e);
        }
        return map;
    }

//    public static void  main(String[] args){
//        sendVipNotice(2,"购购买短信 购买短短信 购短信购买短信 购购买短","2018-08-09",1,"15001303478","sjjsjs");
//        getSendState("15001303478","515912743837150902^0","20181203");
//        System.out.print("【智慧停车云】车主您好，您的月卡车牌变更成功，新车牌为购购买短信 购买短短信 购短信购买短信 购购买短，有效期到2018-08-09".length());
//    }
}
