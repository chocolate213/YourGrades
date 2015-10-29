package com.jxzhang.yourgrades.security;

/**
 * Created by J.X.Zhang on 2015/9/23.
 * @ClassName: cn.zhangjiaxing.SecretUtils
 * @Description: 3DES加密模块
 * @author J.X.Zhang
 * @date 2015-9-23 上午11:28:14
 */
import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONArray;
import org.json.JSONException;


public class DES3Utils {

    // 定义加密算法，DESede即3DES
    private static final String Algorithm = "DESede";
    // 加密密钥
    private static final String PASSWORD_CRYPT_KEY = "cn.zhangjiaxing.secretutils";
    /**
     * 加密方法
     * @param src：要加密数据的字节数组
     * @return
     */
    public static byte[] encryptMode(byte[] src) {
        try {
            // 生成密钥
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            // 实例化Cipher
            Cipher cipher = Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, deskey);  		//加密模式
            return cipher.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 解密方法
     * @param src 密文的字节数组
     * @return
     */
    public static byte[] decryptMode(byte[] src) {
        try {
            SecretKey deskey = new SecretKeySpec(
                    build3DesKey(PASSWORD_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);  			//解密模式
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }

    /**
     * 根据给密钥生成其24位字节数组
     * @param keyStr 秘钥
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = keyStr.getBytes("UTF-8");
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
    /**
     * 将字节数组转换为可以存储在SharedPreference中的字符串
     * @param data
     * @return
     */
    public static String byteArray2String(byte[] data){
        JSONArray jsonArray = new JSONArray();
        for (byte b : data) {
            jsonArray.put(b);
        }
        return jsonArray.toString();
    }
    /**
     * 将从SharedPreference中取出的字符串转换为字节数组
     * @param data	字符串
     * @param len	字节数组长度
     * @return
     * @throws JSONException
     */
    public static byte[] string2byteArray(String data,int len) throws JSONException{
        byte[] bytes = new byte[len];
        JSONArray jsonArray = new JSONArray(data);
        for (int i = 0; i < jsonArray.length(); i++) {
            bytes[i] = (byte)jsonArray.getInt(i);
        }
        return bytes;
    }
}