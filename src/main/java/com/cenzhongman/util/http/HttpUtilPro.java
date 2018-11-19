//package com.cenzhongman.util.http;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URLEncoder;
//
///** 请将此类拷贝到有HttpServletResponse环境下使用，如Tomcat SpringBoot等
// * @author 岑忠满
// * @date 2018/11/19 9:27
// */
//public class HttpUtilPro extends HttpUtil {
//
//    /**
//     * 提供给用户下载的响应
//     *
//     * @param response HttpServletResponse这个文件流将写道这个相应中
//     * @param file     文件
//     */
//    public static void toDownload(HttpServletResponse response, File file) {
//        try {
//            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            // 获取文件的流
//            in = new FileInputStream(file);
//            int len;
//            // 缓存作用
//            byte buf[] = new byte[1024];
//            // 输出流
//            out = response.getOutputStream();
//            while ((len = in.read(buf)) > 0) {
//                out.write(buf, 0, len);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            if (out != null) {
//                try {
//                    out.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 提供给用户下载的响应
//     *
//     * @param response HttpServletResponse这个文件流将写道这个相应中
//     * @param filePath 文件路径
//     */
//    public static void toDownload(HttpServletResponse response, String filePath) {
//        toDownload(response, new File(filePath));
//    }
//}
