package com.cenzhongman.util.file;

import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类，支持读写文件、重命名、删除等，不一定是为了工具，不会写可以看这里的
 *
 * @author 岑忠满
 * @date 2018/9/25 14:09
 */
public class FileUtil {
    private static Logger logger = Logger.getLogger(FileUtil.class);

    // 读
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


    public static List<String> readLines(InputStream in) {
        return readLines(in, StandardCharsets.UTF_8);
    }

    public static List<String> readLines(File file) throws FileNotFoundException {
        return readLines(new FileInputStream(file));
    }

    public static List<String> readLines(File file,Charset charset) throws FileNotFoundException {
        return readLines(new FileInputStream(file),charset);
    }

    public static List<String> readLines(String path, Charset charset) throws FileNotFoundException {
        return readLines(new FileInputStream(new File(path)), charset);
    }

    public static List<String> readLines(String path) throws FileNotFoundException {
        return readLines(new FileInputStream(new File(path)));
    }

    public static String read(InputStream in, Charset charset) {
        StringBuilder sb = new StringBuilder();
        List<String> lines = readLines(in, charset);
        for (String line : lines) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String read(InputStream in) {
        return read(in, StandardCharsets.UTF_8);
    }

    public static String read(InputStream in,Character character) {
        return read(in, character);
    }

    public static String read(String path, Charset charset) throws FileNotFoundException {
        return read(new FileInputStream(new File(path)), charset);
    }

    public static String read(String path) throws FileNotFoundException {
        return read(path, StandardCharsets.UTF_8);
    }

    public static String read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file), StandardCharsets.UTF_8);
    }

    public static String read(File file,Character character) throws FileNotFoundException {
        return read(new FileInputStream(file), character);
    }

    // 写
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

    public static void write(String path, String content, Charset charset, Boolean appEnd) throws FileNotFoundException {
        // 文件夹不存在时创建文件夹，然后再写
        String dirPath = path.replaceAll("[^/\\\\]+$", "");
        if (!exists(dirPath)) {
            if (mkdirs(dirPath)) {
                write(new FileOutputStream(new File(path), appEnd), content, charset);
            } else {
                logger.error("Make dir fail!");
            }
        } else {
            // 文件夹存在直接写
            write(new FileOutputStream(new File(path), appEnd), content, charset);
        }

    }

    public static void write(String path, String content, Charset charset) throws FileNotFoundException {
        write(path, content, charset, false);
    }

    public static void write(String path, String content) throws FileNotFoundException {
        write(path, content, StandardCharsets.UTF_8, false);
    }

    // listFiles
    public static File[] listFiles(File file) {
        File[] files = new File[0];
        if (isDir(file)) {
            files = file.listFiles();
        }
        return files;
    }

    public static File[] listFiles(String path) {
        return listFiles(new File(path));
    }

    // isDir
    public static boolean isDir(String path) {
        return new File(path).isDirectory();
    }

    public static boolean isDir(File file) {
        return file.isDirectory();
    }

    public static boolean isFile(String path) {
        return new File(path).isFile();
    }

    public static boolean isFile(File file) {
        return file.isFile();
    }

    // exists
    public static boolean exists(File file) {
        return file.exists();
    }

    public static boolean exists(String path) {
        return exists(new File(path));
    }

    public static boolean delete(String path) {
        return delete(new File(path));
    }

    public static boolean delete(File file) {
        return file.delete();
    }

    public static boolean renameTo(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(path2);
        return file1.renameTo(file2);
    }

    public static boolean renameTo(File file1, File file2) {
        return file2.renameTo(file2);
    }

    public static boolean mkdir(String path) {
        return new File(path).mkdir();
    }

    public static boolean mkdir(File file) {
        return file.mkdir();
    }

    public static boolean mkdirs(String path) {
        return new File(path).mkdirs();
    }

    public static boolean mkdirs(File file) {
        return file.mkdirs();
    }
}
