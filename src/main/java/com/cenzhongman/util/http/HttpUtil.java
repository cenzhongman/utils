package com.cenzhongman.util.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author 岑忠满
 * @date 2018/9/25 14:02
 */
public class HttpUtil {
    private static final String DEFAULT_CHARSET = "UTF-8";
    /**
     * 设置配置请求参数
     */
    private static RequestConfig requestConfig = RequestConfig.custom()
            // 连接主机服务超时时间
            .setConnectTimeout(35000)
            // 请求超时时间
            .setConnectionRequestTimeout(35000)
            // 数据读取超时时间
            .setSocketTimeout(60000)
            .build();

    /**
     * 普通Http请求
     *
     * @param url url
     * @return 请求结果
     */
    public static String doGet(String url) {
        // 添加头
        Header[] headers = {};
        return doGet(url, headers);
    }

    /**
     * 支持参数编码的get请求
     *
     * @param url 基本URL
     * @param params 参数列表
     * @return 请求结果
     */
    public static String doGet(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        // 添加头
        Header[] headers = {};
        // 将params转为headers
        for (String k : params.keySet()) {
            try {
                String value = URLEncoder.encode(params.get(k),DEFAULT_CHARSET);
                if (!url.contains("?")) {
                    sb.append("?").append(k).append("=").append(value);
                } else {
                    sb.append("&").append(k).append("=").append(value);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = sb.toString();
        }
        return doGet(url, headers);
    }

    /**
     * 支持参数编码的get请求
     *
     * @param url 请求的URL
     * @param headers 请求头
     * @return 请求结果
     */
    private static String doGet(String url, Header[] headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeaders(headers);
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity,DEFAULT_CHARSET);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * Post请求
     *
     * @param url 请求的URL
     * @param paramMap 请求的参数
     * @return 请求结果
     */
    public static String doPost(String url, Map<String, String> paramMap) {
        Header[] headers = {new BasicHeader("Content-Type", "application/x-www-form-urlencoded")};
        return doPost(url, paramMap, headers);
    }


    /**
     * 带请求头的Post请求
     *
     * @param url 请求的URL
     * @param paramMap 请求的参数
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doPost(String url, Map<String, String> paramMap, Header[] headers) {
        String result = "";
        if (null != paramMap && paramMap.size() > 0) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            // 通过map集成entrySet方法获取entity
            Set<Map.Entry<String, String>> entrySet = paramMap.entrySet();
            // 循环遍历，获取迭代器
            for (Map.Entry<String, String> mapEntry : entrySet) {
                nvps.add(new BasicNameValuePair(mapEntry.getKey(), mapEntry.getValue().toString()));
            }

            // 为httpPost设置封装好的请求参数
            try {
                HttpEntity entity = new UrlEncodedFormEntity(nvps, DEFAULT_CHARSET);
                // 表单使用编码格式
                result = doPost(url, entity, headers);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * Post请求
     *
     * @param url 请求的URL
     * @param json 请求的Json
     * @return 请求结果
     */
    public static String doPost(String url, String json) {
        Header[] headers = {new BasicHeader("Content-Type", "application/json")};
        return doPost(url, json, headers);
    }


    /**
     * 带请求头的Post请求
     *
     * @param url 请求的URL
     * @param json 请求的Json
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doPost(String url, String json, Header[] headers) {
        String result = "";
        try {
            HttpEntity entity = new StringEntity(json);
            result = doPost(url, entity, headers);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 最终的Post实现
     *
     * @param url     url
     * @param entity  请求实体
     * @param headers 请求头
     * @return 请求结果
     */
    private static String doPost(String url, HttpEntity entity, Header[] headers) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";
        // 创建httpClient实例
        httpClient = HttpClients.createDefault();
        // 创建httpPost远程连接实例
        HttpPost httpPost = new HttpPost(url);
        // 为httpPost实例设置配置
        httpPost.setConfig(requestConfig);
        // 设置请求头
        httpPost.setHeaders(headers);
        httpPost.setEntity(entity);
        try {
            // httpClient对象执行post请求,并返回响应参数对象
            httpResponse = httpClient.execute(httpPost);
            // 从响应对象中获取响应内容
            HttpEntity responseEntity = httpResponse.getEntity();
            result = EntityUtils.toString(responseEntity,DEFAULT_CHARSET);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != httpResponse) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}