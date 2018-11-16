package com.cenzhongman.util.file;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZipUtilTest {

    @Test
    void zip() {
        ZipUtil.zip("/home/czm/data/zip","/home/czm/data/zip.zip");
    }

    @Test
    void unZip() {
        ZipUtil.unzip("/home/czm/data/zip.zip","/home/czm/data/zip");
    }
}