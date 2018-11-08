package com.cenzhongman.util.iamge;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author 岑忠满
 * @date 2018/11/5 16:41
 */
class ImageUtilTest {

    @Test
    void cutImage() {
        ImageUtil.cutImage("/home/czm/data/0.jpg","/home/czm/data/", 250, 70, 300, 400);
    }

    @Test
    void thumbnailImage() {
        ImageUtil.thumbnailImage("/home/czm/data/0.jpg", 150, 100);
    }
}