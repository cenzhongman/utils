package com.cenzhongman.util.http;

import com.cenzhongman.util.file.FileUtil;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
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
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.io.File.separator;

/**
 * @author 岑忠满
 * @date 2018/9/25 14:02
 */
public class HttpUtil {
    private static Logger logger = Logger.getLogger(HttpUtil.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    public static String doGet(String url) {
        Header[] headers = {};
        return doGet(url, headers);
    }

    /**
     * 普通Http请求
     *
     * @param url url
     * @return 请求结果
     */
    public static String doGet(String url, HttpHost proxy) {
        // 添加头
        Header[] headers = {};
        return doGet(url, headers, proxy);
    }

    /**
     * 支持参数编码的get请求
     *
     * @param url    基本URL
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
                String value = URLEncoder.encode(params.get(k), DEFAULT_CHARSET);
                if (!url.contains("?")) {
                    sb.append("?").append(k).append("=").append(value);
                } else {
                    sb.append("&").append(k).append("=").append(value);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = sb.toString();
            logger.info("Get:"+url);
        }
        return doGet(url, headers);
    }

    public static String doGet(String url, Header[] headers) {
        return doGet(url, headers, null);
    }

    /**
     * 支持参数编码的get请求
     *
     * @param url     请求的URL
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doGet(String url, Header[] headers, HttpHost proxy) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = null;

        RequestConfig requestConfig = RequestConfig.custom()
                // 连接主机服务超时时间
                .setConnectTimeout(300000)
                // 请求超时时间
                .setConnectionRequestTimeout(300000)
                // 数据读取超时时间
                .setSocketTimeout(300000)
                .setProxy(proxy)
                .build();

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

            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(entity, DEFAULT_CHARSET);
            }

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
     * @param url      请求的URL
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
     * @param url      请求的URL
     * @param paramMap 请求的参数
     * @param headers  请求头
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
     * @param url  请求的URL
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
     * @param url     请求的URL
     * @param json    请求的Json
     * @param headers 请求头
     * @return 请求结果
     */
    public static String doPost(String url, String json, Header[] headers) {
        String result = "";
        HttpEntity entity = new StringEntity(json, DEFAULT_CHARSET);
        result = doPost(url, entity, headers);
        return result;
    }

    private static String doPost(String url, HttpEntity entity, Header[] headers) {
        return doPost(url, entity, headers, null);
    }


    /**
     * 最终的Post实现
     *
     * @param url     url
     * @param entity  请求实体
     * @param headers 请求头
     * @return 请求结果
     */
    private static String doPost(String url, HttpEntity entity, Header[] headers, HttpHost proxy) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        String result = "";

        RequestConfig requestConfig = RequestConfig.custom()
                // 连接主机服务超时时间
                .setConnectTimeout(35000)
                // 请求超时时间
                .setConnectionRequestTimeout(35000)
                // 数据读取超时时间
                .setSocketTimeout(60000)
                .setProxy(proxy)
                .build();

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
            result = EntityUtils.toString(responseEntity, DEFAULT_CHARSET);
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

    /**
     * 下载文件或图片，使用文件自身的文件名
     *
     * @param urlString    下载链接
     */
    public static void download(String urlString, File downloadFile) throws IOException {
        download(urlString, downloadFile.getParent(), downloadFile.getName());
    }

    /**
     * 下载文件或图片，使用文件自身的文件名
     *
     * @param urlString    下载链接
     * @param downloadPath 下载路径
     */
    public static void download(String urlString, String downloadPath) throws IOException {
        download(urlString, downloadPath, "");
    }

    /**
     * 下载文件或图片
     *
     * @param urlString    下载链接
     * @param downloadPath 下载路径
     * @param saveName     保存的文件名
     */
    public static void download(String urlString, String downloadPath, String saveName) throws IOException {
        URL url;
        String fileName;
        String savePath;

        // 若下载文件夹不存在创建文件夹
        if (!FileUtil.exists(downloadPath)) {
            FileUtil.mkdirs(downloadPath);
        }

        // 文件名包含路径，抛出异常
        if (saveName.contains("\\") || saveName.contains("/")) {
            throw new IllegalArgumentException("名字不应包含路径");
        }

        // 为下载文件夹添加后缀
        if (!downloadPath.endsWith("\\") && !downloadPath.endsWith("/")) {
            downloadPath = downloadPath + separator;
        }

        url = new URL(urlString);
        DataInputStream dataInputStream = new DataInputStream(url.openStream());

        URLConnection uc = url.openConnection();

        // 下载名不存在，使用默认名字
        if ("".equals(saveName)) {
            try {
                fileName = uc.getHeaderField("Content-Disposition");
                fileName = new String(fileName.getBytes(StandardCharsets.ISO_8859_1), "GBK");
                fileName = URLDecoder.decode(fileName.substring(fileName.indexOf("filename=") + 9), "UTF-8");
            } catch (NullPointerException e) {
                fileName = urlString.replaceAll("^.*[/\\\\]", "");
            }
            saveName = fileName;
        }

        savePath = downloadPath + saveName;

        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int length;

        while ((length = dataInputStream.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        byte[] context = output.toByteArray();
        fileOutputStream.write(output.toByteArray());
        dataInputStream.close();
        fileOutputStream.close();
    }
}