package com.cenzhongman.util.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author 岑忠满
 * @date 2018/10/19 16:44
 */
public class FileUtilTest {
    @Test
    void readLines() {
        System.out.println(FileUtil.readLines(this.getClass().getResourceAsStream("/FileUtilTest.txt")));
        System.out.println(FileUtil.readLines(this.getClass().getResourceAsStream("/FileUtilTest.txt"), StandardCharsets.UTF_8));
        try {
            System.out.println(FileUtil.readLines("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt"));
            System.out.println(FileUtil.readLines("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt", StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void read() {
        System.out.println(FileUtil.read(this.getClass().getResourceAsStream("/FileUtilTest.txt")));
        System.out.println(FileUtil.read(this.getClass().getResourceAsStream("/FileUtilTest.txt"), StandardCharsets.UTF_8));
    }

    @Test
    void write() {
        FileUtil.write("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt", "1\n2\n3", StandardCharsets.UTF_8);
        FileUtil.write("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt", "4\n5\n6", StandardCharsets.UTF_8, true);
        FileUtil.write("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt", "7\n8\n9");

    }

    @Test
    void getName() {
        System.out.println(FileUtil.getName("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\"));
        System.out.println(FileUtil.getNameWithOutType("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\123.txt"));
    }

    @Test
    void listFile() {
        System.out.println(FileUtil.listFiles("C:\\Users\\34566\\OneDrive\\program\\utils\\src\\test\\resources\\FileUtilTest.txt"));
    }

    @Test
    void delete(){
        System.out.println(FileUtil.delete("C:\\home\\czm\\data\\bmwData\\急啊急啊就\\"));
    }
}
