package com.czandlh.utils;

import java.security.MessageDigest;

public class EncryptionUtils {

  public static String encryptMD5(String salt, String text) {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      String toEntryp = salt + "@$#" + text;
      digest.update(toEntryp.getBytes());
      return encodeHex(digest.digest());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static final String encodeHex(byte[] bytes) {
    StringBuffer buf = new StringBuffer(bytes.length * 2);
    int i;
    for (i = 0; i < bytes.length; i++) {
      if (((int) bytes[i] & 0xff) < 0x10) {
        buf.append("0");
      }
      buf.append(Long.toString((int) bytes[i] & 0xff, 16));
    }
    return buf.toString();
  }

}
