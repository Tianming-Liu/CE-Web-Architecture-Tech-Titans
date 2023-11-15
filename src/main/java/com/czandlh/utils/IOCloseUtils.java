package com.czandlh.utils;

import java.io.InputStream;
import java.io.OutputStream;

public class IOCloseUtils {

    public static void close(InputStream is) {
        if (null == is) {
            return;
        }
        try {
            is.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(OutputStream os) {
        if (null == os) {
            return;
        }
        try {
            os.flush();
            os.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
