package com.jiucai.mall.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MD5Util {
    /**
     * MD5加密
     * @param message
     * @return
     */
    public static String MD5Encrypt(String message){
        MessageDigest md=null;
        try {
            md=MessageDigest.getInstance("MD5");
            md.reset();
            md.update(message.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {
            log.error("MD5加密异常:"+e);
        } catch (UnsupportedEncodingException e) {
            log.error("MD5加密异常:"+e);
        }
        byte[] byteArray=md.digest();
        StringBuffer md5StrBuff=new StringBuffer();
        for(int i=0;i<byteArray.length;++i){
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            }else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();

    }

}
