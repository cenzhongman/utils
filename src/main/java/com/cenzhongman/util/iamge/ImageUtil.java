package com.cenzhongman.util.iamge;


import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;

/**
 * <p>Title: ImageUtil </p>
 * <p>Description: </p>
 * <p>Email: icerainsoft@hotmail.com </p>
 *
 * @author 岑忠满
 * @date 2014年10月28日 上午10:24:26
 */
public class ImageUtil {
    private static Logger logger = Logger.getLogger(ImageUtil.class);

    /**
     * 默认缩略图前缀
     */
    private static String DEFAULT_THUMB_PREVFIX = "thumb_";
    /**
     * 默认切图前缀
     */
    private static String DEFAULT_CUT_PREVFIX = "cut_";
    /**
     * 默认不拉伸
     */
    private static Boolean DEFAULT_FORCE = false;


    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     *
     * @param srcImg 源图片
     * @param output 图片输出流
     * @param rect   需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, OutputStream output, java.awt.Rectangle rect) {
        if (srcImg.exists()) {
            java.io.FileInputStream fis = null;
            ImageInputStream iis = null;
            try {
                fis = new FileInputStream(srcImg);
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if (srcImg.getName().contains(".")) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if (suffix == null || !types.toLowerCase().contains(suffix.toLowerCase() + ",")) {
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return;
                }
                // 将FileInputStream 转换为ImageInputStream
                iis = ImageIO.createImageInputStream(fis);
                // 根据图片类型获取该种类型的ImageReader
                ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
                reader.setInput(iis, true);
                ImageReadParam param = reader.getDefaultReadParam();
                param.setSourceRegion(rect);
                BufferedImage bi = reader.read(0, param);
                ImageIO.write(bi, suffix, output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (iis != null) {
                        iis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.warn("the src image is not exist.");
        }
    }

    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     *
     * @param srcImg  源图片
     * @param output  图片输出流
     * @param x,y,w,h 需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, OutputStream output, int x, int y, int w, int h) {
        cutImage(srcImg, output, new java.awt.Rectangle(x, y, w, h));
    }

    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     *
     * @param srcImg      源图片
     * @param destImgPath 图片输出路径
     * @param rect        需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, String destImgPath, java.awt.Rectangle rect) {
        File destImg = new File(destImgPath);
        String saveImg;

        if (destImg.exists() || destImgPath.contains(".")) {
            String p = destImg.getPath();

            try {
                if (!destImg.isDirectory()) {
                    p = destImg.getParent();
                    if (!new File(p).exists()) {
                        new File(p).mkdirs();
                    }
                    saveImg = destImg.getAbsolutePath();
                } else {
                    saveImg = p + DEFAULT_CUT_PREVFIX + srcImg.getName();
                }
                if (!p.endsWith(File.separator)) {
                    p = p + File.separator;
                }
                cutImage(srcImg, new java.io.FileOutputStream(saveImg), rect);
            } catch (FileNotFoundException e) {
                logger.warn("the dest image is not exist.");
            }
        } else {
            logger.warn("the dest image folder is not exist.");
            destImg.mkdirs();
        }
    }

    /**
     * <p>Title: cutImage</p>
     * <p>Description:  根据原图与裁切size截取局部图片</p>
     *
     * @param srcImg      源图片
     * @param destImgPath 图片输出路径
     * @param x,y,w,h     需要截取部分的坐标和大小
     */
    public static void cutImage(File srcImg, String destImgPath, int x, int y, int w, int h) {
        cutImage(srcImg, destImgPath, new java.awt.Rectangle(x, y, w, h));
    }

    public static void cutImage(String srcImg, String destImg, int x, int y, int width, int height) {
        cutImage(new File(srcImg), destImg, new java.awt.Rectangle(x, y, width, height));
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param srcImg 原图片路径
     * @param output 图片输出流
     * @param w      缩略图宽
     * @param h      缩略图高
     * @param force  是否拉伸
     */
    public static void thumbnailImage(File srcImg, OutputStream output, int w, int h, boolean force) {
        if (srcImg.exists()) {
            try {
                // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
                String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
                String suffix = null;
                // 获取图片后缀
                if (srcImg.getName().contains(".")) {
                    suffix = srcImg.getName().substring(srcImg.getName().lastIndexOf(".") + 1);
                }// 类型和图片后缀全部小写，然后判断后缀是否合法
                if (suffix == null || !types.toLowerCase().contains(suffix.toLowerCase() + ",")) {
                    logger.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                    return;
                }
                BufferedImage img = ImageIO.read(srcImg);

                int width = img.getWidth(null);
                int height = img.getHeight(null);

                BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                image.getGraphics().drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

                int startX = 0;
                int startY = 0;
                int endX = 0;
                int endY = 0;
                int newWeight = 0;
                int newHeight = 0;
                // 根据原图与要求的缩略图尺寸，找到最合适的缩略图比例
                if (!force) {
                    if ((width * 1.0) / w < (height * 1.0) / h) {
                        // 原图比例更高
                        newWeight = Integer.parseInt(new java.text.DecimalFormat("0").format((width * 1.0) * (h * 1.0 / height)));
                        newHeight = h;
                        startX = (w / 2) - (newWeight / 2);
                        startY = 0;
                    } else {
                        // 原图比例更宽
                        newWeight = w;
                        newHeight = Integer.parseInt(new java.text.DecimalFormat("0").format((height * 1.0) * (w * 1.0 / width)));
                        startX = 0;
                        startY = (h / 2) - (newHeight / 2);
                    }
                }


                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics g = bi.getGraphics();
                /* 参数说明
                                -------------------
                                |画板              |
                (startX,startY) |-----------------|
                                |原图              |
                                |                 |(newHeight)
                                |                 |
                                |-----newWidth----|(endX,endY)
                                |                 |
                                -------------------
                */
                g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), startX, startY, newWeight, newHeight, null);
                g.dispose();
                // 将图片保存在原目录并加上前缀
                ImageIO.write(bi, suffix, output);
                output.close();
            } catch (IOException e) {
                logger.error("generate thumbnail image failed.", e);
            }
        } else {
            logger.warn("the src image is not exist.");
        }
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param srcImg  原图片路径
     * @param w       缩略图宽
     * @param h       缩略图高
     * @param prevfix 生成缩略图的前缀
     * @param force   是否拉伸
     */
    public static void thumbnailImage(File srcImg, int w, int h, String prevfix, boolean force) {
        String p = srcImg.getAbsolutePath();
        try {
            if (!srcImg.isDirectory()) {
                p = srcImg.getParent();
            }
            if (!p.endsWith(File.separator)) {
                p = p + File.separator;
            }
            thumbnailImage(srcImg, new java.io.FileOutputStream(p + prevfix + srcImg.getName()), w, h, force);
        } catch (FileNotFoundException e) {
            logger.error("the dest image is not exist.", e);
        }
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param imagePath 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param prevfix   生成缩略图的前缀
     * @param force     是否拉伸
     */
    public static void thumbnailImage(String imagePath, int w, int h, String prevfix, boolean force) {
        File srcImg = new File(imagePath);
        thumbnailImage(srcImg, w, h, prevfix, force);
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param imagePath 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     * @param force     是否拉伸
     */
    public static void thumbnailImage(String imagePath, int w, int h, boolean force) {
        thumbnailImage(imagePath, w, h, DEFAULT_THUMB_PREVFIX, force);
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param imagePath 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     */
    public static void thumbnailImage(String imagePath, int w, int h) {
        thumbnailImage(imagePath, w, h, DEFAULT_FORCE);
    }

    /**
     * <p>Title: thumbnailImage</p>
     * <p>Description: 根据图片路径生成缩略图 </p>
     *
     * @param imagePath 原图片路径
     * @param w         缩略图宽
     * @param h         缩略图高
     */
    public static void thumbnailImage(String imagePath, String savePath, int w, int h) {
        try {
            thumbnailImage(new File(imagePath), new FileOutputStream(savePath), w, h, DEFAULT_FORCE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void thumbnail(String imagePath, String savePath, int w, int h) {
        try {
            Thumbnails.of(imagePath)
                    //                    .keepAspectRatio(false)
                    //                    .size(w,h)
                    .forceSize(w, h)
                    //                    .outputQuality(1f)
                    .toFile(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}