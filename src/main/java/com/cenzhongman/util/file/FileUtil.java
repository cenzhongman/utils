package com.cenzhongman.util.file;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 文件工具类，支持读写文件、重命名、删除等
 *
 * @author 岑忠满
 * @date 2018/9/25 14:09
 */
public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);

    /**
     * 指定格式读取文件
     *
     * @param in      输入流
     * @param charset 文件编码
     * @return 文件的行
     */
    public static List<String> readLines(InputStream in, Charset charset) {
        List<String> lineList = new ArrayList<>();

        if (in == null) {
            throw new NullPointerException("InputStream is empty!");
        }

        BufferedReader bufferedReader = null;
        String line;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in, charset));
            while ((line = bufferedReader.readLine()) != null) {
                lineList.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                in.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return lineList;
    }

    /**
     * 读取文件
     *
     * @param in 输入流
     * @return 文件的所有行
     */
    public static List<String> readLines(InputStream in) {
        return readLines(in, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件
     *
     * @param file 文件
     * @return 文件的行
     */
    public static List<String> readLines(File file) throws FileNotFoundException {
        return readLines(new FileInputStream(file));
    }

    /**
     * 指定格式读取文件
     *
     * @param file    文件
     * @param charset 文件编码
     * @return 文件的行
     */
    public static List<String> readLines(File file, Charset charset) throws FileNotFoundException {
        return readLines(new FileInputStream(file), charset);
    }

    /**
     * 指定格式读取文件
     *
     * @param path    文件路径
     * @param charset 文件编码
     * @return 文件的行
     */
    public static List<String> readLines(String path, Charset charset) throws FileNotFoundException {
        return readLines(new FileInputStream(new File(path)), charset);
    }

    /**
     * 读取文件
     *
     * @param path 文件路径
     * @return 文件的行
     */
    public static List<String> readLines(String path) throws FileNotFoundException {
        return readLines(new FileInputStream(new File(path)));
    }

    /**
     * 读取文件
     *
     * @param in      输入流
     * @param charset 文件编码
     * @return 文件的内容
     */
    public static String read(InputStream in, Charset charset) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, charset));
        try {
            int value;
            while ((value = bufferedReader.read()) != -1) {
                stringBuilder.append((char) value);
            }
        } catch (IOException ignored) {
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 读取文件
     *
     * @param in 输入流
     * @return 文件的内容
     */
    public static String read(InputStream in) {
        return read(in, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件
     *
     * @param path    文件路径
     * @param charset 文件编码
     * @return 文件的内容
     */
    public static String read(String path, Charset charset) throws FileNotFoundException {
        return read(new FileInputStream(new File(path)), charset);
    }

    /**
     * 读取文件
     *
     * @param path 文件路径
     * @return 文件的内容
     */
    public static String read(String path) throws FileNotFoundException {
        return read(path, StandardCharsets.UTF_8);
    }

    /**
     * 读取文件
     *
     * @param file 文件
     * @return 文件的内容
     */
    public static String read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件
     *
     * @param file    文件
     * @param charset 文件编码
     * @return 文件的内容
     */
    public static String read(File file, Charset charset) throws FileNotFoundException {
        return read(new FileInputStream(file), charset);
    }

    /**
     * 写入文件
     *
     * @param out     输入流
     * @param content 内容
     * @param charset 编码
     */
    private static void write(OutputStream out, String content, Charset charset) {
        BufferedWriter bufferedWriter;
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(out, charset));
        try {
            bufferedWriter.write(content);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                bufferedWriter.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 写入文件
     *
     * @param path    写出的路径，若不存在则自动创建
     * @param content 内容
     * @param charset 编码
     */
    public static void write(String path, String content, Charset charset, Boolean appEnd) {
        // 文件夹不存在时创建文件夹，然后再写
        String dirPath = path.replaceAll("[^/\\\\]+$", "");
        if (!exists(dirPath)) {
            if (mkdirs(dirPath)) {
                try {
                    write(new FileOutputStream(new File(path), appEnd), content, charset);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                logger.error("Make dir fail!");
            }
        } else {
            // 文件夹存在直接写
            try {
                write(new FileOutputStream(new File(path), appEnd), content, charset);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 写入文件
     *
     * @param file    写出的文件，若不存在则自动创建
     * @param content 内容
     * @param charset 编码
     */
    public static void write(File file, String content, Charset charset, Boolean appEnd) {
        write(file.getAbsolutePath(), content, charset, appEnd);
    }

    /**
     * 写入文件
     *
     * @param path    写出的路径，若不存在则自动创建
     * @param content 内容
     * @param charset 编码
     */
    public static void write(String path, String content, Charset charset) {
        write(path, content, charset, false);
    }

    /**
     * 写入文件
     *
     * @param file    写出的文件，若不存在则自动创建
     * @param content 内容
     * @param charset 编码
     */
    public static void write(File file, String content, Charset charset) {
        write(file.getAbsolutePath(), content, charset);
    }

    /**
     * 写入文件
     *
     * @param file    写出的文件，若不存在则自动创建
     * @param content 内容
     */
    public static void write(File file, String content) {
        write(file, content, StandardCharsets.UTF_8, false);
    }

    /**
     * 写入文件
     *
     * @param path    写出的路径，若不存在则自动创建
     * @param content 内容
     */
    public static void write(String path, String content) {
        write(path, content, StandardCharsets.UTF_8, false);
    }

    /**
     * 列举当前文件夹下的所有文件
     *
     * @param file 文件夹
     * @return 若为文件夹，返回所有文件Array，否则返回空Array
     */
    public static List<File> listFiles(File file) {
        List<File> files = new ArrayList<>();
        if (isDir(file)) {
            files = Arrays.asList(Objects.requireNonNull(file.listFiles()));
        }
        return files;
    }

    /**
     * 列举当前文件夹下的所有文件
     *
     * @param path 文件夹路径
     * @return 若为文件夹，返回所有文件Array，否则返回空Array
     */
    public static List<File> listFiles(String path) {
        return listFiles(new File(path));
    }

    /**
     * 判断是否文件夹
     *
     * @param path 文件夹路径
     * @return 若为文件夹返回True
     */
    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    /**
     * 判断是否文件夹
     *
     * @param file 文件夹
     * @return 若为文件夹返回True
     */
    public static boolean isDir(File file) {
        return file.isDirectory();
    }

    /**
     * 判断是否文件
     *
     * @param path 文件夹路径
     * @return 若为文件返回True
     */
    public static boolean isFile(String path) {
        return new File(path).isFile();
    }

    /**
     * 判断是否文件
     *
     * @param file 文件
     * @return 若为文件返回True
     */
    public static boolean isFile(File file) {
        return file.isFile();
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param file 文件
     * @return 若存在返回True
     */
    public static boolean exists(File file) {
        return file.exists();
    }

    /**
     * 判断文件或文件夹是否存在
     *
     * @param path 文件路径
     * @return 若存在返回True
     */
    public static boolean exists(String path) {
        return exists(new File(path));
    }

    /**
     * 删除文件或文件夹
     *
     * @param path 文件路径
     * @return 成功删除返回True
     */
    public static boolean delete(String path) {
        return delete(new File(path));
    }

    /**
     * 删除文件或文件夹
     * 递归实现
     *
     * @param file 文件
     * @return 成功删除返回True
     */
    public static boolean delete(File file) {
        if (isDir(file)) {
            for (File file1 : listFiles(file)) {
                delete(file1);
            }
        }
        return file.delete();
    }

    /**
     * 重命名文件或文件夹
     *
     * @param srcPath 原名
     * @param dstPath 新名
     * @return 成功重命名返回True
     */
    public static boolean renameTo(String srcPath, String dstPath) {
        File file1 = new File(srcPath);
        File file2 = new File(dstPath);
        return file1.renameTo(file2);
    }

    /**
     * 重命名文件或文件夹
     *
     * @param srcFile 原名
     * @param dstFile 新名
     * @return 成功重命名返回True
     */
    public static boolean renameTo(File srcFile, File dstFile) {
        return srcFile.renameTo(dstFile);
    }

    /**
     * 创建文件夹
     *
     * @param path 文件夹
     * @return 成功创建返回True
     */
    public static boolean mkdir(String path) {
        return new File(path).mkdir();
    }

    /**
     * 创建文件夹
     *
     * @param file 文件夹
     * @return 成功创建返回True
     */
    public static boolean mkdir(File file) {
        return file.mkdir();
    }

    /**
     * 递归创建文件夹
     *
     * @param path 文件夹
     * @return 成功创建返回True
     */
    public static boolean mkdirs(String path) {
        return new File(path).mkdirs();
    }

    /**
     * 递归创建文件夹
     *
     * @param file 文件夹
     * @return 成功创建返回True
     */
    public static boolean mkdirs(File file) {
        return file.mkdirs();
    }

    /**
     * 复制文件
     *
     * @param srcFile 原文件
     * @param dstFile 新文件
     */
    public static void copyFile(File srcFile, File dstFile) throws IOException {
        Files.copy(srcFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 复制文件
     *
     * @param srcPath 原路径
     * @param dstPath 新路径
     */
    public static void copyFile(String srcPath, String dstPath) throws IOException {
        copyFile(new File(srcPath), new File(dstPath));
    }

    /**
     * 复制文件夹
     *
     * @param srcFile 原文件夹
     * @param dstFile 新文件夹
     */
    public static void copyDir(File srcFile, File dstFile) throws IOException {
        FileUtils.copyDirectory(srcFile, dstFile);
    }

    /**
     * 复制文件夹
     *
     * @param srcPath 原路径
     * @param dstPath 新路径
     */
    public static void copyDir(String srcPath, String dstPath) throws IOException {
        FileUtils.copyDirectory(new File(srcPath), new File(dstPath));
    }


    /**
     * 移动文件夹
     *
     * @param srcFile 原文件夹
     * @param dstFile 新文件夹
     */
    public static void moveDir(File srcFile, File dstFile) throws IOException {
        FileUtils.moveDirectory(srcFile, dstFile);
    }

    /**
     * 移动文件夹
     *
     * @param srcPath 原路径
     * @param dstPath 新路径
     */
    public static void moveDir(String srcPath, String dstPath) throws IOException {
        FileUtils.moveDirectory(new File(srcPath), new File(dstPath));
    }

    /**
     * 获取文件夹名
     *
     * @param file 文件或文件夹路径
     * @return 文件或文件夹名, 若为文件带后缀
     */
    public static String getName(File file) {
        return file.getName();
    }

    /**
     * 获取文件夹名
     *
     * @param path 文件或文件夹路径
     * @return 文件或文件夹名, 若为文件带后缀
     */
    public static String getName(String path) {
        return getName(new File(path));
    }


    /**
     * 获取文件夹名, 且不带后缀
     *
     * @param file 文件或文件夹
     * @return 文件或文件夹名, 若为文件不带后缀
     */
    public static String getNameWithOutType(File file) {
        return getName(file).replaceAll("\\.[a-zA-Z]+?$", "");
    }

    /**
     * 获取文件夹名, 且不带后缀
     *
     * @param path 文件或文件夹路径
     * @return 文件或文件夹名, 若为文件不带后缀
     */
    public static String getNameWithOutType(String path) {
        return getNameWithOutType(new File(path));
    }
}
