package com.coderjj.phonedefend.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2019/5/6.
 */

public class StreamUtil {

    /**
     * @param is 流对象
     * @return 流转换成的字符串 返回null代表异常
     */
    public static String streamToString(InputStream is) {
        //1.在读取的过程中，将读取的内容存储到缓存中，然后一次性转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流操作，读到没有为之
        byte[] buffer = new byte[1024];
        //3.记录读取内容的临时变量
        int temp;
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            //返回读取数据
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
