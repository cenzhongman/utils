package com.cenzhongman.util.iamge;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 岑忠满
 * @date 2018/11/5 16:41
 */
class ImageUtilTest {

    // 将你的资源文件路径复制到这里
    String rootDir = "C:\\Users\\34566\\OneDrive\\program\\utils\\src\\main\\resources\\";

    @Test
    void cutImage() {
        ImageUtil.cutImage("/home/czm/data/0.jpg", "/home/czm/data/", 250, 70, 300, 400);
    }

    @Test
    void thumbnailImage() throws FileNotFoundException {
        // 原图比例：50*100
        // 情况1 原图比例比较高，且需要放大
        ImageUtil.thumbnailImage(rootDir + "test.png", rootDir + "test1.png", 400, 200);
        // 情况2 原图比例比较高，且需要缩小
        ImageUtil.thumbnailImage(rootDir + "test.png", rootDir + "test2.png", 100, 50);
        // 情况3：新图比例比较高，且需要放大
        ImageUtil.thumbnailImage(rootDir + "test.png", rootDir + "test3.png", 60, 200);
        // 情况4：新图比例比较高，且需要缩小
        ImageUtil.thumbnailImage(rootDir + "test.png", rootDir + "test4.png", 40, 100);
    }
}