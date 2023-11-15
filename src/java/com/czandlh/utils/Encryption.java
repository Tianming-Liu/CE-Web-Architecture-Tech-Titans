package com.czandlh.utils;

import org.apache.commons.codec.digest.Md5Crypt;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.util.Base64;

public class Encryption {

    private static String sKey = "rest@126.com"; // encryption key

    private static String salt = "$1$asd"; // encryption key

    /**
     * md5加盐加密
     * @param password
     * @return
     */
    public static String md5Crypt(String password) {
        return Md5Crypt.md5Crypt(password.getBytes(), salt);
    }

    // create a secret key
    private static SecretKey makeKeyFactory() throws Exception {
        SecretKeyFactory des = SecretKeyFactory.getInstance("DES");// DES algorithm
        SecretKey secretKey = des.generateSecret(new DESKeySpec(sKey.getBytes()));
        return secretKey;
    }

    //get an encrypted string using DES algorithm
    public static String encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return new String(Base64.getEncoder().encode(cipher.doFinal(text.getBytes())));
    }

    // retrive a normal string from an encrypted string
    public static String decrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("DES");
        SecretKey secretKey = makeKeyFactory();
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(text.getBytes())));
    }

    public static void main(String[] args) throws Exception{
        System.out.println(md5Crypt("123456"));
    }
}

