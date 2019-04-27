package com.pinyougou.common.util;

/**
 * @ClassName MD5Utils
 * @Description TODO
 * @Author lilei
 * @Date 26/04/2019 15:21
 * @Version 1.0
 **/

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {

    private static final Logger logger = LoggerFactory.getLogger(MD5Utils.class);

    private static MessageDigest messagedigest = null;

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 messagedigest初始化失败", e);
        }
    }

    /**
     * 对一个文件获取md5值
     *
     * @return md5串
     */
    public static String getFileMD5String(File file) {

        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            return getStreamMD5String(fileInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getStreamMD5String(InputStream input) {
        try {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = input.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(messagedigest.digest()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getMD5String(String s) {
        return DigestUtils.md5Hex(s);
    }

    public static String getMD5String(byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }

    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }
}

