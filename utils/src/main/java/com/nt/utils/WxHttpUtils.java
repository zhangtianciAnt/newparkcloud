package com.nt.utils;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.nt.utils.dao.WeixinException;
import com.nt.utils.dao.WxBaseRequest;
import com.nt.utils.dao.WxBaseResponse;
import com.nt.utils.dao.WxSupport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
/**
 * 微信网络访问工具类
 *
 * @author WangSong
 */
public class WxHttpUtils {

    // 日志对象
    protected final static Logger logger = LoggerFactory.getLogger(WxHttpUtils.class);

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * get请求返回json格式字符串
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String fetch(String url) throws IOException {
        String result = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpclient.execute(httpget);
        try {
            String responseContent = null;
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity, "UTF-8");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = responseContent;
            }
        } finally {
            response.close();
        }

        return result;
    }

    /**
     * post请求返回json格式字符串
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String post(String url, String data) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            String responseContent = null;

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data, Charset.forName("UTF-8")));

            response = httpclient.execute(httpPost);
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = responseContent;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

    /**
     * 上传文件请求返回json格式字符串
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String postFile(String url, File file) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            String responseContent = null;
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);

            multipartEntityBuilder.addPart("media", new FileBody(file));

            HttpEntity entity = multipartEntityBuilder.build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(entity);

            response = httpclient.execute(httpPost);
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = responseContent;
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }

        return result;
    }

    /**
     * 微信接口post请求
     *
     * @param url
     * @param wxBaseRequest
     * @param clasz
     * @return
     * @throws WeixinException
     */
    public static WxBaseResponse post(String url, WxBaseRequest wxBaseRequest, Class<? extends WxBaseResponse> clasz)
            throws WeixinException {
        try {
            String jsonData = JSON.toJSONString(wxBaseRequest);
            String jsonObject = WxHttpUtils.post(url, jsonData);

            WxBaseResponse wxBaseResponse = JSON.parseObject(jsonObject, clasz);
            int errCode = wxBaseResponse.getErrcode();
            if (errCode != 0) { throw new WeixinException(WxSupport.getCause(errCode)); }

            return wxBaseResponse;
        } catch (IOException e) {
            logger.error("网络访问出错:{}",e);
            throw new WeixinException("网络访问出错！");
        }
    }

    /**
     * 微信接口post请求
     *
     * @param url
     * @param map
     * @param clasz
     * @return
     * @throws WeixinException
     */
    @SuppressWarnings("rawtypes")
    public static WxBaseResponse post(String url, Map map, Class<? extends WxBaseResponse> clasz) throws WeixinException {
        try {
            String jsonData = JSON.toJSONString(map);
            String jsonObject = WxHttpUtils.post(url, jsonData);

            WxBaseResponse wxBaseResponse = JSON.parseObject(jsonObject, clasz);
            int errCode = wxBaseResponse.getErrcode();
            if (errCode != 0) { throw new WeixinException(WxSupport.getCause(errCode)); }

            return wxBaseResponse;
        } catch (IOException e) {
            logger.error("网络访问出错:{}",e);
            throw new WeixinException("网络访问出错！");
        }
    }

    public static String httpsRequest(String url, String method) throws Exception {
        InputStream in = null;
        OutputStream out = null;
        String str_return = "";
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestMethod(method);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            DataInputStream indata = new DataInputStream(is);
            String ret = "";

            while (ret != null) {
                ret = indata.readLine();
                if (ret != null && !ret.trim().equals("")) {
                    str_return = str_return + new String(ret.getBytes("ISO-8859-1"), "UTF-8");
                }
            }
            conn.disconnect();
        } catch (ConnectException e) {
            System.out.println("ConnectException");
            System.out.println(e);
            throw e;

        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println(e);
            throw e;

        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return str_return;
    }

    /**
     * POST请求并传json数据
     *
     * @param url
     * @param method
     * @param json
     * @return
     * @throws Exception
     */
    public static String httpsRequestWithJson(String url, String method, String json)
            throws Exception {
        InputStream in = null;
        DataOutputStream out = null;
        String str_return = "";
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            URL console = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestMethod(method);
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            out = new DataOutputStream(conn.getOutputStream());
            out.write(json.getBytes("UTF-8"));
            InputStream is = conn.getInputStream();
            DataInputStream indata = new DataInputStream(is);
            String ret = "";
            while (ret != null) {
                ret = indata.readLine();
                if (ret != null && !ret.trim().equals("")) {
                    str_return = str_return + new String(ret.getBytes("ISO-8859-1"), "UTF-8");
                }
            }
            conn.disconnect();
        } catch (ConnectException e) {
            System.out.println("ConnectException");
            System.out.println(e);
            throw e;

        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println(e);
            throw e;

        } finally {
            try {
                in.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
        return str_return;
    }

    /**
     * 获取http访问后的json串
     *
     * @param ip
     * @param port
     * @param projectName
     * @param urlOrAction
     * @param params
     * @return
     */
    public static String getJSONContent(String urlPath) {
        String jsonString = "";
        // 访问路径
        try {
            // 创建HTTP连接
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "*/*");
            connection.setDoOutput(true);

            int code = connection.getResponseCode();

            if (code == 200) {
                InputStream is = connection.getInputStream();
                DataInputStream indata = new DataInputStream(is);
                String ret = "";

                while (ret != null) {
                    ret = indata.readLine();
                    if (ret != null && !ret.trim().equals("")) {
                        jsonString = jsonString + new String(ret.getBytes("ISO-8859-1"), "UTF-8");
                    }
                }
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public static String getJSONContent(String urlPath, String json) {
        String jsonString = "";
        // 访问路径
        DataOutputStream out = null;
        try {
            // 创建HTTP连接
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "*/*");
            connection.setDoOutput(true);
            out = new DataOutputStream(connection.getOutputStream());
            out.write(json.getBytes("UTF-8"));
            int code = connection.getResponseCode();
            if (code == 200) {
                InputStream is = connection.getInputStream();
                DataInputStream indata = new DataInputStream(is);
                String ret = "";
                while (ret != null) {
                    ret = indata.readLine();
                    if (ret != null && !ret.trim().equals("")) {
                        jsonString = jsonString + new String(ret.getBytes("ISO-8859-1"), "UTF-8");
                    }
                }
                connection.disconnect();
            } else {
                logger.info("response code:" + code + " msg :" + connection.getResponseMessage());
            }
        } catch (Exception e) {
            logger.error("http 请求异常:", e);
        }
        return jsonString;
    }

    /**
     * @param requestUrl 请求地址
     * @param method 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @throws Exception
     */
    public static InputStream httpsRequest(String requestUrl, String method, String outputStr) {
        HttpsURLConnection connection = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());

            URL url = new URL(requestUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);

            // 设置请求方式（GET/POST）
            connection.setRequestMethod(method);
            if ("GET".equalsIgnoreCase(method)) {
                connection.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = connection.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            return connection.getInputStream();
        } catch (Exception e) {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

}
