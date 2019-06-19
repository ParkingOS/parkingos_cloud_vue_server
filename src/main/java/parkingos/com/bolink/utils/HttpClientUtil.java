package parkingos.com.bolink.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 通用https请求,自动区分https和http,分表单提交和raw提交
 * @time 2017-8-21
 * @author QuanHao
 */
public class HttpClientUtil {

    static Logger logger = Logger.getLogger(HttpClientUtil.class);
	public static final int connTimeout=20000;
    public static final int readTimeout=20000;
    public static final String DEFAULT_CHARSET="UTF-8";
    private static final String METHOD_POST = "POST";
    private static HttpClient client = null;

    public static final String SunX509 = "SunX509";
    public static final String JKS = "JKS";
    public static final String PKCS12 = "PKCS12";
    public static final String TLS = "TLS";
    
    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(128);
        cm.setDefaultMaxPerRoute(128);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }
    
    public static String postParameters(String url, String parameterStr) throws ConnectTimeoutException, SocketTimeoutException, Exception{
        return post(url,parameterStr,"application/x-www-form-urlencoded",DEFAULT_CHARSET,connTimeout,readTimeout);
    }
    
    public static String postParameters(String url, String parameterStr,String charset, Integer connTimeout, Integer readTimeout) throws ConnectTimeoutException, SocketTimeoutException, Exception{
        return post(url,parameterStr,"application/x-www-form-urlencoded",charset,connTimeout,readTimeout);
    }
    
    public static String postParameters(String url, Map<String, Object> params) throws ConnectTimeoutException,
     SocketTimeoutException, Exception {
         return postForm(url, params, null, connTimeout, readTimeout);
     }
    
    public static String postParameters(String url, Map<String, Object> params, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,
    SocketTimeoutException, Exception {
         return postForm(url, params, null, connTimeout, readTimeout);
     }
    public static String postParameter(String url, Map<String, String> params) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        return postForms(url, params, null, connTimeout, readTimeout);
    }

    public static String postParameter(String url, Map<String, String> params, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {
        return postForms(url, params, null, connTimeout, readTimeout);
    }
    public static String get(String url) throws Exception {  
        return get(url, DEFAULT_CHARSET, null, null);
    }
    
    public static String get(String url, String charset) throws Exception {  
        return get(url, charset, connTimeout, readTimeout);  
    }

    public static InputStream String2Inputstream(String str) {
        return new ByteArrayInputStream(str.getBytes());
    }

    /**
     * 提交form表单
     *
     * @param url
     * @param params
     * @param connTimeout
     * @param readTimeout
     * @return
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException
     * @throws Exception
     */
    public static String postForms(String url, Map<String, String> params, Map<String, String> headers, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {

        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Entry<String, String>> entrySet = params.entrySet();
                for (Entry<String, String> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = HttpClientUtil.client;
                res = client.execute(post);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }

    /** 
     * 发送一个 Post 请求, 使用指定的字符集编码. 
     *  
     * @param url 
     * @param body RequestBody 
     * @param mimeType 例如 application/xml "application/x-www-form-urlencoded" a=1&b=2&c=3
     * @param charset 编码 
     * @param connTimeout 建立链接超时时间,毫秒. 
     * @param readTimeout 响应超时时间,毫秒. 
     * @return ResponseBody, 使用指定的字符集编码. 
     * @throws ConnectTimeoutException 建立链接超时异常
     * @throws SocketTimeoutException  响应超时 
     * @throws Exception 
     */  
    public static String post(String url, String body, String mimeType,String charset, Integer connTimeout, Integer readTimeout) 
            throws ConnectTimeoutException, SocketTimeoutException, Exception {
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        String result = "";
        try {
            if (StringUtils.isNotBlank(body)) {
                HttpEntity entity = new StringEntity(body, ContentType.create(mimeType, charset));
                post.setEntity(entity);
            }
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());

            HttpResponse res;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = HttpClientUtil.client;
                res = client.execute(post);
            }
            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null&& client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }  
    
    
    /** 
     * 提交form表单 
     *  
     * @param url 
     * @param params 
     * @param connTimeout 
     * @param readTimeout 
     * @return 
     * @throws ConnectTimeoutException
     * @throws SocketTimeoutException 
     * @throws Exception 
     */  
    public static String postForm(String url, Map<String, Object> params, Map<String, String> headers, Integer connTimeout,Integer readTimeout) throws ConnectTimeoutException,
            SocketTimeoutException, Exception {  
  
        HttpClient client = null;
        HttpPost post = new HttpPost(url);
        try {  
            if (params != null && !params.isEmpty()) {  
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                Set<Entry<String, Object>> entrySet = params.entrySet();
                for (Entry<String, Object> entry : entrySet) {
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()+""));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                post.setEntity(entity);
            }

            if (headers != null && !headers.isEmpty()) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            post.setConfig(customReqConf.build());
            HttpResponse res = null;
            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(post);
            } else {
                // 执行 Http 请求.
                client = HttpClientUtil.client;
                res = client.execute(post);
            }
            return IOUtils.toString(res.getEntity().getContent(), "UTF-8");
        } finally {
            post.releaseConnection();
            if (url.startsWith("https") && client != null
                    && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
    }




    /**
     * 发送一个 GET 请求
     *
     * @param url
     * @param charset
     * @param connTimeout  建立链接超时时间,毫秒.
     * @param readTimeout  响应超时时间,毫秒.
     * @return
     * @throws ConnectTimeoutException   建立链接超时
     * @throws SocketTimeoutException   响应超时
     * @throws Exception
     */
    public static String get(String url, String charset, Integer connTimeout,Integer readTimeout)
            throws ConnectTimeoutException,SocketTimeoutException, Exception {

        HttpClient client = null;
        HttpGet get = new HttpGet(url);
        String result = "";
        try {
            // 设置参数
            Builder customReqConf = RequestConfig.custom();
            if (connTimeout != null) {
                customReqConf.setConnectTimeout(connTimeout);
            }
            if (readTimeout != null) {
                customReqConf.setSocketTimeout(readTimeout);
            }
            get.setConfig(customReqConf.build());

            HttpResponse res = null;

            if (url.startsWith("https")) {
                // 执行 Https 请求.
                client = createSSLInsecureClient();
                res = client.execute(get);
            } else {
                // 执行 Http 请求.
                client = HttpClientUtil.client;
                res = client.execute(get);
            }

            result = IOUtils.toString(res.getEntity().getContent(), charset);
        } finally {
            get.releaseConnection();
            if (url.startsWith("https") && client != null && client instanceof CloseableHttpClient) {
                ((CloseableHttpClient) client).close();
            }
        }
        return result;
    }


    /**
     * 从 response 里获取 charset
     *
     * @param ressponse
     * @return
     */
    @SuppressWarnings("unused")
    private static String getCharsetFromResponse(HttpResponse ressponse) {
        // Content-Type:text/html; charset=GBK
        if (ressponse.getEntity() != null  && ressponse.getEntity().getContentType() != null && ressponse.getEntity().getContentType().getValue() != null) {
            String contentType = ressponse.getEntity().getContentType().getValue();
            if (contentType.contains("charset=")) {
                return contentType.substring(contentType.indexOf("charset=") + 8);
            }
        }
        return null;
    }



    /**
     * 创建 SSL连接
     * @return
     * @throws GeneralSecurityException
     */
    private static CloseableHttpClient createSSLInsecureClient() throws GeneralSecurityException {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                        public boolean isTrusted(X509Certificate[] chain,String authType) throws CertificateException {
                            return true;
                        }
                    }).build();

            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                        @Override
                        public boolean verify(String arg0, SSLSession arg1) {
                            return true;
                        }

                        @Override
                        public void verify(String host, SSLSocket ssl)
                                throws IOException {
                        }

                        @Override
                        public void verify(String host, X509Certificate cert)
                                throws SSLException {
                        }

                        @Override
                        public void verify(String host, String[] cns,
                                String[] subjectAlts) throws SSLException {
                        }

                    });

            return HttpClients.custom().setSSLSocketFactory(sslsf).build();

        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    public static String postHttpsJSON(String url, String params) throws Exception {
        String ctype = "application/json;charset=" + DEFAULT_CHARSET;
        byte[] content = {};
        if(params != null){
            content = params.getBytes(DEFAULT_CHARSET);
        }
        return postHttpsJSON(url, ctype, content, readTimeout, readTimeout);
    }

    public static String postHttpsJSON(String url, String ctype, byte[] content,
                                int connectTimeout,int readTimeout) throws Exception {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            try{
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()},
                        new SecureRandom());
                SSLContext.setDefault(ctx);

                conn = getConnection(new URL(url), METHOD_POST, ctype);
                conn.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
            }catch(Exception e){
                logger.error("GET_CONNECTOIN_ERROR, URL = " + url, e);
                throw e;
            }
            try{
                out = conn.getOutputStream();
                out.write(content);
                rsp = getResponseAsString(conn);
            }catch(IOException e){
                logger.error("REQUEST_RESPONSE_ERROR, URL = " + url, e);
                throw e;
            }

        }finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return rsp;
    }

    private static class DefaultTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private static HttpsURLConnection getConnection(URL url, String method, String ctype)
            throws IOException {
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html");
        conn.setRequestProperty("User-Agent", "stargate");
        conn.setRequestProperty("Content-Type", ctype);
        return conn;
    }

    protected static String getResponseAsString(HttpURLConnection conn) throws IOException {
        String charset = getResponseCharset(conn.getContentType());
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), charset);
        } else {
            String msg = getStreamAsString(es, charset);
            if (msg==null||"".equals(msg)) {
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            } else {
                throw new IOException(msg);
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
            StringWriter writer = new StringWriter();
            char[] chars = new char[256];
            int count = 0;
            while ((count = reader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    private static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;
        if (ctype!=null&&!"".equals(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (pair[1]!=null&&!"".equals(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }

//    public static void main(String[] args) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("id","啊好烦");
//        try {
//            String s = HttpClientUtil.postParameters("http://127.0.0.1/api-server/dataupload", params, 20000, 20000);
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 获取不带查询串的url
     * @param strUrl
     * @return String
     */
    public static String getURL(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(0, indexOf);
            }

            return strUrl;
        }

        return strUrl;

    }

    /**
     * 获取查询串
     * @param strUrl
     * @return String
     */
    public static String getQueryString(String strUrl) {

        if(null != strUrl) {
            int indexOf = strUrl.indexOf("?");
            if(-1 != indexOf) {
                return strUrl.substring(indexOf+1, strUrl.length());
            }

            return "";
        }

        return strUrl;
    }

    /**
     * 获取CA证书信息
     * @param cafile CA证书文件
     * @return Certificate
     * @throws CertificateException
     * @throws IOException
     */
    public static Certificate getCertificate(File cafile)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(cafile);
        Certificate cert = cf.generateCertificate(in);
        in.close();
        return cert;
    }

    /**
     * 存储ca证书成JKS格式
     * @param cert
     * @param alias
     * @param password
     * @param out
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    public static void storeCACert(Certificate cert, String alias,
                                   String password, OutputStream out) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(null, null);

        ks.setCertificateEntry(alias, cert);

        // store keystore
        ks.store(out, HttpClientUtil.str2CharArray(password));

    }

    /**
     * 字符串转换成char数组
     * @param str
     * @return char[]
     */
    public static char[] str2CharArray(String str) {
        if(null == str) return null;

        return str.toCharArray();
    }


    /**
     * 获取SSLContext
     * @param trustPasswd
     * @param keyPasswd
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws IOException
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws KeyManagementException
     */
    public static SSLContext getSSLContext(
            FileInputStream trustFileInputStream, String trustPasswd,
            FileInputStream keyFileInputStream, String keyPasswd)
            throws NoSuchAlgorithmException, KeyStoreException,
            CertificateException, IOException, UnrecoverableKeyException,
            KeyManagementException {

        // ca
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore trustKeyStore = KeyStore.getInstance(HttpClientUtil.JKS);
        trustKeyStore.load(trustFileInputStream, HttpClientUtil
                .str2CharArray(trustPasswd));
        tmf.init(trustKeyStore);

        final char[] kp = HttpClientUtil.str2CharArray(keyPasswd);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(HttpClientUtil.SunX509);
        KeyStore ks = KeyStore.getInstance(HttpClientUtil.PKCS12);
        ks.load(keyFileInputStream, kp);
        kmf.init(ks, kp);

        SecureRandom rand = new SecureRandom();
        SSLContext ctx = SSLContext.getInstance(HttpClientUtil.TLS);
        ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), rand);

        return ctx;
    }


    /**
     * get HttpURLConnection
     * @param strUrl url地址
     * @return HttpURLConnection
     * @throws IOException
     */
    public static HttpURLConnection getHttpURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        return httpURLConnection;
    }


    /**
     * get HttpsURLConnection
     * @param strUrl url地址
     * @return HttpsURLConnection
     * @throws IOException
     */
    public static HttpsURLConnection getHttpsURLConnection(String strUrl)
            throws IOException {
        URL url = new URL(strUrl);
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                .openConnection();
        return httpsURLConnection;
    }

    /**
     * BufferedReader转换成String<br/>
     * 注意:流关闭需要自行处理
     * @param reader
     * @return String
     * @throws IOException
     */
    public static String bufferedReader2String(BufferedReader reader) throws IOException {
        StringBuffer buf = new StringBuffer();
        String line = null;
        while( (line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\r\n");
        }

        return buf.toString();
    }

    /**
     * 处理输出<br/>
     * 注意:流关闭需要自行处理
     * @param out
     * @param data
     * @param len
     * @throws IOException
     */
    public static void doOutput(OutputStream out, byte[] data, int len)
            throws IOException {
        int dataLen = data.length;
        int off = 0;
        while (off < data.length) {
            if (len >= dataLen) {
                out.write(data, off, dataLen);
                off += dataLen;
            } else {
                out.write(data, off, len);
                off += len;
                dataLen -= len;
            }

            // 刷新缓冲区
            out.flush();
        }

    }

}
