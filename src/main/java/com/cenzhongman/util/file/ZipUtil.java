package com.cenzhongman.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Zip压缩/解压缩工具类
 * 实现对目标路径及其子路径下的所有文件及空目录的压缩
 *
 * @author 岑忠满
 */
public class ZipUtil {
    /**
     * 缓冲器大小
     */
    private static final int BUFFER = 512;

    /**
     * 解压缩方法
     *
     * @param zipFileName 压缩文件名
     * @param dstPath     解压目标路径
     * @return 是否解压成功
     */
    public static boolean unzip(String zipFileName, String dstPath) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileName));
            ZipEntry zipEntry;
            // 缓冲器
            byte[] buffer = new byte[BUFFER];
            // 每次读出来的长度
            int readLength;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                // 若是zip条目目录，则需创建这个目录
                if (zipEntry.isDirectory()) {
                    File dir = new File(dstPath + "/" + zipEntry.getName());

                    if (!dir.exists()) {
                        dir.mkdirs();
                        continue;
                    }
                }

                // 若是文件，则需创建该文件
                File file = createFile(dstPath, zipEntry.getName());

                OutputStream outputStream = new FileOutputStream(file);

                while ((readLength = zipInputStream.read(buffer, 0, BUFFER)) != -1) {
                    outputStream.write(buffer, 0, readLength);
                }
                outputStream.close();
            }
            zipInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 压缩方法
     * （可以压缩空的子目录）
     *
     * @param srcPath     压缩源路径
     * @param zipFileName 目标压缩文件
     * @return 是否压缩成功
     */
    public static boolean zip(String srcPath, String zipFileName) {
        File srcFile = new File(srcPath);
        List<File> fileList = getAllFiles(srcFile);
        byte[] buffer = new byte[BUFFER];
        ZipEntry zipEntry;
        int readLength;

        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            for (File file : fileList) {
                if (file.isFile()) {
                    //若是文件，则压缩这个文件
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file));
                    zipEntry.setSize(file.length());
                    zipEntry.setTime(file.lastModified());
                    zipOutputStream.putNextEntry(zipEntry);

                    InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                    while ((readLength = inputStream.read(buffer, 0, BUFFER)) != -1) {
                        zipOutputStream.write(buffer, 0, readLength);
                    }

                    inputStream.close();
                } else {
                    //若是目录（即空目录）则将这个目录写入zip条目
                    zipEntry = new ZipEntry(getRelativePath(srcPath, file) + "/");
                    zipOutputStream.putNextEntry(zipEntry);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (zipOutputStream != null) {
                    zipOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 取的给定源目录下的所有文件及空的子目录
     * 递归实现
     *
     * @param srcFile 目标文件夹
     * @return 所有当级所有文件(文件带路径的)
     */
    private static List<File> getAllFiles(File srcFile) {
        List<File> fileList = new ArrayList<File>();
        File[] tmp = srcFile.listFiles();

        assert tmp != null;
        for (File aTmp : tmp) {

            if (aTmp.isFile()) {
                fileList.add(aTmp);
            }

            if (aTmp.isDirectory()) {
                if (Objects.requireNonNull(aTmp.listFiles()).length != 0) {
                    // 若不是空目录，则递归添加其下的目录和文件
                    fileList.addAll(getAllFiles(aTmp));
                } else {
                    // 若是空目录，则添加这个目录到fileList
                    fileList.add(aTmp);
                }
            }
        }

        return fileList;
    }

    /**
     * 取相对路径
     * 依据文件名和压缩源路径得到文件在压缩源路径下的相对路径
     *
     * @param dirPath 压缩源路径
     * @param file    需要压缩文件
     * @return 相对路径
     */
    private static String getRelativePath(String dirPath, File file) {
        File dir = new File(dirPath);
        StringBuilder relativePath = new StringBuilder(file.getName());

        while (true) {
            file = file.getParentFile();

            if (file == null) {
                break;
            }

            if (file.equals(dir)) {
                break;
            } else {
                relativePath.insert(0, file.getName() + "/");
            }
        }

        return relativePath.toString();
    }

    /**
     * 创建文件
     * 根据压缩包内文件名和解压缩目的路径，创建解压缩目标文件，生成中间目录
     *
     * @param dstPath  解压缩目的路径
     * @param fileName 压缩包内文件名
     * @return 解压缩目标文件
     */
    private static File createFile(String dstPath, String fileName) {
        // 将文件名的各级目录分解
        String[] dirs = fileName.split("/");
        File file = new File(dstPath);

        if (dirs.length > 1) {
            //文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                // 依次创建文件对象知道文件的上一级目录
                file = new File(file, dirs[i]);
            }

            if (!file.exists()) {
                // 文件对应目录若不存在，则创建
                file.mkdirs();
            }
            // 创建文件
            file = new File(file, dirs[dirs.length - 1]);

            return file;
        } else {
            if (!file.exists()) {
                // 若目标路径的目录不存在，则创建
                file.mkdirs();
            }
            // 创建文件
            file = new File(file, dirs[0]);

            return file;
        }
    }
}