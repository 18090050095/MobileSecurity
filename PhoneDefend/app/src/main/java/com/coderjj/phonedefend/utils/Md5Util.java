package com.coderjj.phonedefend.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2019/5/10.
 */

public class Md5Util {
    /**
     * 给指定字符串按照md5算法加密
     *
     * @param psd
     * @return
     */
    public static String encoder(String psd) {
        try {
            //加盐处理
            psd = psd + "phonedefend";
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(psd.getBytes());
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bs) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
