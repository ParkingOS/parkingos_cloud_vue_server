package parkingos.com.bolink.utils;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * 七牛文件上传类
 *
 */
public class QiNiuFileUtil {

    private static Logger logger = LoggerFactory.getLogger(QiNiuFileUtil.class);
    //七牛的AccessKey/Secret Key
    private static String accessKey ="lPu7qAg2w8GQyt4lqY_7JK7qqMvHZIcxzlAxpCRk";
    private static String secretKey ="X8y6ks03Jp6ON7V44_O9YAfyAkzBQ5a4fQAPfre3";
    private static String bucket = "carimage";
    private static String imgDomain = "https://carimg.bolink.club/";//图片域名

    public static  String upload(InputStream fileStream){
        String key = null;
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        try {

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(fileStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                logger.info("upload ret ,key: "+putRet.key+" hash: "+putRet.hash);
                return imgDomain+putRet.hash;

            } catch (QiniuException ex) {
                Response r = ex.response;
                logger.error("QiniuException:",ex);
                System.err.println(r.toString());
                try {
                    logger.info(r.bodyString());
                } catch (QiniuException ex2) {
                    logger.error("QiniuException2:",ex2);
                }
            }
        } catch (Exception ex) {
            logger.error("Qiniu allException:",ex);
            return null;
        }

        return null;
    }

}
