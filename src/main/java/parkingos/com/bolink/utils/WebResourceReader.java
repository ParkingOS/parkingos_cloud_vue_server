package parkingos.com.bolink.utils;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class WebResourceReader {

    private static final Logger log = LoggerFactory.getLogger(WebResourceReader.class);
    protected static final Logger STDERR = LoggerFactory.getLogger("STDERR");
    private static String encoding = "gbk";

    private static String getHost(String url){
        String regexp = "(http://)?([^/]*)(/?.*)";
        return url.replaceAll(regexp, "$2");
    }

    private static String getPath(String url){
        return url.replaceAll("http://"+getHost(url), "");
    }

//    public static String read(String url) {
//        StringBuffer buffer = new StringBuffer();
//        BufferedReader in = null;
//        GZIPInputStream gzin = null;
//        GetMethod getMethod = null;
//        try {
//            PostUrl pu = new PostUrl();
//            pu.setHost(getHost(url));
//            pu.setPath(getPath(url));
//            pu.setPort(80);
//            // 创建GET方法的实例
//            getMethod = HttpClientUtil.getInstance().get(pu);
//            // 使用系统提供的默认的恢复策略
//            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//                    new DefaultHttpMethodRetryHandler());
//            // 执行getMethod
//            int statusCode = getMethod.getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                log.error("读取远程资源文件失败: "+ url);
//            }
//            InputStream is = getMethod.getResponseBodyAsStream();
//            String contEncoding ="";
//            Header head = getMethod.getResponseHeader(
//                    "Content-Encoding");
//            if(null!=head){
//                contEncoding = head.getValue();
//            }
//            /**
//             * 如果文件使用GZIP压缩，则用GZIP流进行处理
//             */
//            if (StringUtils.isNotBlank(contEncoding)
//                    && contEncoding.contains("gzip")) {
//                // 读取内容
//                gzin = new GZIPInputStream(is);
//                in = new BufferedReader(new InputStreamReader(gzin, encoding));
//            } else {
//                in = new BufferedReader(new InputStreamReader(is, encoding));
//            }
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                buffer.append(inputLine);
//            }
//        }catch (Exception e) {
//            log.error("##read error=",e);
//        } finally {
//            // 释放连接
//            if(getMethod!=null){
//                getMethod.releaseConnection();
//            }
//            try {
//                if(in!=null)
//                    in.close();
//            } catch (IOException e) {
//                log.error("Read remote file exception : ",e);
//            }
//        }
//        return buffer.toString();
//    }

    /**
     * HttpClientUtil get方法
     * @param url
     * @return
     * @throws Exception
     */
//    public static String getHttpResponse(String url) throws Exception{
//        BufferedReader in = null;
//        InputStream is = null;
//        GetMethod getMethod  = null;
//        try {
//            PostUrl pu = new PostUrl();
//            pu.setHost(getHost(url));
//            pu.setPath(getPath(url));
//            pu.setPort(80);
//            getMethod = HttpClientUtil.getInstance().get(pu);
//            getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
//            int statusCode = getMethod.getStatusCode();
//            log.info("##getHttpResponse-url="+url+"|http-status="+ statusCode);
//            is = getMethod.getResponseBodyAsStream();
//            StringBuffer buffer = new StringBuffer();
//            in = new BufferedReader(new InputStreamReader(is, "utf-8"));
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                buffer.append(inputLine);
//            }
//            return buffer.toString();
//        } catch (Exception e) {
//            STDERR.error("##getHttpResponse error=",e);
//            throw e;
//        }finally{
//            try {
//                if(is!=null)
//                    is.close();
//                if(in!=null)
//                    in.close();
//                if(getMethod!=null)
//                    getMethod.releaseConnection();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * HttpURLConnection Post方法
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String param) throws Exception {
        URL url1 = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        HttpURLConnection conn = null;
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            int resCode = conn.getResponseCode();
            log.info("##doPost……url="+url+",param="+param+", Response code is " + resCode);
            if(resCode==200)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            return "";
        } catch (IOException e) {
            STDERR.error("##HTTP Request is error,url="+url+",param="+param+", error=",e);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doPost  error1:",e1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    STDERR.error("##doPost  error2:",e);
                }
            }
            if (conn != null)
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doPost  error3:",e);
                }
        }
    }

    /**
     * headers的value暂时支持1个参数
     * @param url
     * @param param
     * @param headers
     * @return
     */
//    public static String doPost(String url, String param, HttpHeaders headers) throws Exception {
//        Iterator iterator = headers.entrySet().iterator();
//        HashMap<Object, Object> map = new HashMap<Object, Object>();
//        while (iterator.hasNext()) {
//            HttpHeaders.Entry entry = (HttpHeaders.Entry) iterator.next();
//            Object key = entry.getKey();
//            Object vals = entry.getValue();
//            if (vals instanceof LinkedList) {
//                String val = (String) ((LinkedList) vals).get(0);
//                if (val instanceof String && org.apache.commons.lang3.StringUtils.isNotBlank(val)) {
//                    map.put(key, val);
//                }
//            }
//        }
//        return doPost(url, param, map);
//    }

    /**
     * HttpURLConnection Post方法，待测
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String doPost(String url, String param, Map headers) throws Exception {
        URL url1 = null;
        BufferedReader reader = null;
        DataOutputStream writer = null;
        GZIPInputStream gzin = null;
        HttpURLConnection conn = null;
        BufferedReader input = null;
        String encodeCharset = "UTF-8";
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            Iterator iterator = headers.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (key instanceof String && val instanceof String)
                    conn.setRequestProperty((String) key, (String) val);
            }
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            writer = new DataOutputStream(conn.getOutputStream());
            writer.write(param.getBytes());
            writer.flush();
            writer.close();
            int resCode = conn.getResponseCode();
            log.info("##doPost_header_url=" + url + ",param=" + param + ", Response code is " + resCode);
            if (resCode == 200) {
                String acceptEncoding = conn.getRequestProperty("accept-encoding");

                /**
                 * 如果文件使用GZIP压缩，则用GZIP流进行处理
                 */
                if (StringUtils.isNotBlank(acceptEncoding)
                        && acceptEncoding.contains("gzip")) {
                    // 读取内容
                    gzin = new GZIPInputStream(conn.getInputStream());
                    input = new BufferedReader(new InputStreamReader(gzin,encodeCharset));
                } else {
                    input = new BufferedReader(new InputStreamReader(conn.getInputStream(),encodeCharset));
                }

                String result = "";
                String str;
                while (null != (str = input.readLine())) {
                    result += str;
                }
                return result;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            STDERR.error("##HTTP Request is error,url=" + url + ",param=" + param + ", error=", e);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doPost  error1:", e1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    STDERR.error("##doPost  error2:", e);
                }
            }
            if (conn != null)
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doPost  error3:", e);
                }
        }
    }

    /**
     * HttpURLConnection get方法
     * @param url
     * @return
     */
    public static String doGet(String url) {
        URL url1 = null;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            if (conn.getResponseCode()== 200)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            return "";
        } catch (IOException e) {
            STDERR.error("##doGet error,url="+url+", error=",e);
            throw new RuntimeException(e.getMessage(),e);
        } finally {
            try {
                if(reader!=null)reader.close();
                if(conn!=null)conn.disconnect();
            } catch (Exception e) {
                STDERR.error("##doGet finally error,url="+url+", error=",e);
                e.printStackTrace();
            }
        }
    }
    /**
     * HttpURLConnection PUT方法
     * @param url
     * @return
     */
    public static String doPUT(String url,Map<String,String> headers) {
        URL url1 = null;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("PUT");
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            conn.connect();
            int code = conn.getResponseCode();
            if (code == 200) {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            log.info("##doPut code" + code);
            return "";
        } catch (IOException e) {
            STDERR.error("##doPut error,url="+url+", error=",e);
            throw new RuntimeException(e.getMessage(),e);
        } finally {
            try {
                if(reader!=null)reader.close();
                if(conn!=null)conn.disconnect();
            } catch (Exception e) {
                STDERR.error("##doPut error1,url="+url+", error=",e);
            }
        }
    }

    /**
     * HttpURLConnection get方法
     * @param url
     * @return
     */
    public static String doGet(String url, String userAgent, Boolean useUserAgent,String clientIp) {
        URL url1 = null;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);

            if(useUserAgent) {
                conn.addRequestProperty("User-Agent", userAgent);
            }
            if(StringUtils.isNotBlank(clientIp)){
                conn.addRequestProperty("j-forwarded-for",clientIp);
                conn.addRequestProperty("x-forwarded-for",clientIp);
            }

            conn.connect();
            if (conn.getResponseCode()== 200)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            return "";
        } catch (IOException e) {
            STDERR.error("##doGet error,url="+url+", error=",e);
            throw new RuntimeException(e.getMessage(),e);
        } finally {
            try {
                if(reader!=null)reader.close();
                if(conn!=null)conn.disconnect();
            } catch (Exception e) {
                STDERR.error("##doGet finally error,url="+url+", error=",e);
                e.printStackTrace();
            }
        }
    }

    /**
     * HttpURLConnection get方法
     * @param url
     * @return
     */
    public static String doGet(String url,String charset) {
        URL url1 = null;
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        if(StringUtils.isBlank(charset)){
            charset = "utf-8";
        }
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(true);
            conn.connect();
            if (conn.getResponseCode()== 200)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),charset));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }

            return "";
        } catch (IOException e) {
            STDERR.error("##doGet error,url="+url+", error=",e);
            throw new RuntimeException(e.getMessage(),e);
        } finally {
            try {
                if(reader!=null)reader.close();
                if(conn!=null)conn.disconnect();
            } catch (Exception e) {
                STDERR.error("##doGet finally error,url="+url+", error=",e);
                e.printStackTrace();
            }
        }
    }


    /**
     * HttpURLConnection Post方法获取请求
     * @param url
     * @param param
     * @return
     */
    public static Map<String,Object> doPostForLive(String url, String param){
        URL url1 = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        HttpURLConnection connection = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            writer = new PrintWriter(connection.getOutputStream());
            writer.print(param);
            writer.flush();
            int resCode = connection.getResponseCode();
            map.put("rescode", resCode);
            log.info("##doPostForLive……url="+url+",param="+param+", Response code is " + resCode);
            if(resCode!= HttpStatus.SC_OK){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            map.put("resmess", sb.toString());
        } catch (Exception e) {
            STDERR.error("##doPostForLive Request is error,url="+url+",param="+param+", error=",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doPostForLive  error1:",e1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    STDERR.error("##doPostForLive  error2:",e);
                }
            }
            if (connection != null)
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doPostForLive  error3:",e);
                }
        }
        return map;

    }

    /**
     * HttpURLConnection Get方法获取请求
     * @param url
     * @return
     */
    public static Map<String,Object> doGetForLive(String url){
        URL url1 = null;
        BufferedReader reader = null;
        HttpURLConnection connection = null;
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(20000);//设置连接主机超时（单位：毫秒）
            connection.setReadTimeout(20000);//设置从主机读取数据超时（单位：毫秒）
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            int resCode = connection.getResponseCode();
            log.info("##doGetLive……url="+url+", Response code is " + resCode);
            map.put("rescode", resCode);
            if(resCode!= HttpStatus.SC_OK){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(),"UTF-8"));
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            }
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            map.put("resmess", sb.toString());

        } catch (Exception e) {
            STDERR.error("##doGetLive Request is error,url="+url+", error=",e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doGetLive  error1:",e1);
                }
            }
            if (connection != null)
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doGetLive  error3:",e);
                }
        }
        return map;

    }
    /**
     * 抽奖
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String doPostForLottery(String url, String param) throws Exception {
        URL url1 = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        HttpURLConnection connection = null;
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
            connection.setRequestProperty("referer", "http://live.jd.com");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            writer = new PrintWriter(connection.getOutputStream());
            writer.print(param);
            writer.flush();
            int resCode = connection.getResponseCode();
            log.info("##doPostForLottery……url="+url+",param="+param+", Response code is " + resCode);
            if(resCode==200)
            {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            return "";
        } catch (IOException e) {
            STDERR.error("##doPostForLottery HTTP Request is error,url="+url+",param="+param+", error=",e);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doPostForLottery  error1:",e1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    STDERR.error("##doPostForLottery  error2:",e);
                }
            }
            if (connection != null)
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doPostForLottery  error3:",e);
                }
        }
    }

    /**
     * 获取外部数据 try3次
     *
     * @param url
     * @return
     */
    public static String getDataTry(String url, String param, String type) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        log.info("##getDataTry----url=" + url + "|param=" + param + "|type="+ type);
        String data = StringUtils.EMPTY;
        // 失败尝试3次
        int try_num = 3;
        for (int retryTime = 1; retryTime <= try_num; retryTime++) {
            try {
                if (StringUtils.isNotBlank(type) && "POST".equals(type)) {
                    data = WebResourceReader.doPost(url, param);
                } else if (StringUtils.isNotBlank(type) && "GET".equals(type)) {
                    data = WebResourceReader.doGet(url);
                }
                break;
            } catch (Exception e) {
                if (retryTime == try_num) {
                    STDERR.error("##getDataTry...url" + url + "|try" + try_num
                            + "次 fail |param=" + param + "|type=" + type
                            + "|error=", e);
                } else {
                    log.info("##getDataTry...url=" + url + "|try="
                            + retryTime);
                    continue;
                }
            }
        }
        log.info("##getDataTry...url=" + url + "|param=" + param + "|type="
                + type + "|data=" + data);
        return data;
    }


    /**
     * HttpURLConnection Post方法   参数json格式
     * @param url
     * @param param
     * @return
     * @throws Exception
     */
    public static String doPostJson(String url, String param) throws Exception {
        URL url1 = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        HttpURLConnection conn = null;
        try {
            url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            writer = new PrintWriter(conn.getOutputStream());
            writer.print(param);
            writer.flush();
            int resCode = conn.getResponseCode();
            log.info("##doPost……url="+url+",param="+param+", Response code is " + resCode);
            if(resCode==200)
            {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            }
            return "";
        } catch (IOException e) {
            STDERR.error("##HTTP Request is error,url="+url+",param="+param+", error=",e);
            throw e;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    STDERR.error("##doPost  error1:",e1);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    STDERR.error("##doPost  error2:",e);
                }
            }
            if (conn != null)
                try {
                    conn.disconnect();
                } catch (Exception e) {
                    STDERR.error("##doPost  error3:",e);
                }
        }
    }

}