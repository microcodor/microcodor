package com.wxdroid.basemodule.network.utils;


import android.text.TextUtils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HttpEncryptUtils {
//    private static final String key = "1234567890";
//    private static final String CHARSET = "UTF-8";

    public static String encryptWithAESAndBase64(String src) {
//        EsUtils.setKey(AppConstants.ES_SECURITY_KEY);
//        if(!TextUtils.isEmpty(src)) {
//            return EsUtils.encode(src);
//        }
        return "";
//        return encryptAES(src, Security.convertKey(key));
    }

    public static String decryptWithBase64AndAES(String src) throws Exception
    {
//        EsUtils.setKey(AppConstants.ES_SECURITY_KEY);
//        if(!TextUtils.isEmpty(src)) {
//            return EsUtils.decode(src);
//        }
        return "";
//        return decryptAESEx(src, Security.convertKey(key));
    }

//    public static final String key = "1234567890";
    public static final String key = "DPF5HCBTSJXZVKQX";
    private static final String CHARSET = "UTF-8";
    public static String encryptAES(String value, String privateKey) {
        try {
            byte[] raw = privateKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return Base64.encode((cipher.doFinal(value.getBytes(CHARSET))));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static String decryptAESEx(String value, String privateKey) {
        if (value == null || value.trim().length() == 0) {
            return value;
        }
        try {
            byte[] raw = privateKey.getBytes("ASCII");
            System.out.println(raw.length);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] bytes = cipher.doFinal(Base64.decode(value));
            return new String(bytes).replaceAll("[\\s*\t\n\r]", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
