
package com.wxdroid.basemodule.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class SecurityUtils
{

    private static MessageDigest md5;// MD5
    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算二进制的MD5，返回十六进制串
     * 
     * @param input 需要加密的byte数组
     * @return
     */
    public static String encryptMD5(byte[] input) {
        return ByteConvertor.bytesToHexString(MD5(input));
    }

    /**
     * 计算字符串MD5，返回十六进制
     * 
     * @param input 需要加密的字符串
     * @return
     */
    public static String encryptMD5(String input) {
        if (input == null) {
            return "";
        }
        return encryptMD5(input.getBytes());
    }
    
    /**
     * 计算文件 MD5，返回十六进制串
     * 
     * @param fileName 传入文件
     * @return
     */
    public static String encryptFileMD5(String fileName) {
        if (fileName == null) {
            return "";//尽量不要返回null，这样就可以直接对比了
        }
        return encryptMD5(new File(fileName));
    }

    /**
     * 计算文件 MD5，返回十六进制串
     * 
     * @param input 传入文件
     * @return
     */
    public static String encryptMD5(File input) {
        if (!input.exists()) {
            return "";//不要返回
        }
        byte[] digest = MD5(input);
        String md5 = ByteConvertor.bytesToHexString(digest);
        if(md5 == null)
            return "";
        else 
            return md5;
    }
    
    

    /**
     * 计算输入流的MD5，返回十六进制串
     * 
     * @param inputStream 需要加密的流
     * @return
     */
    public static String encryptMD5(InputStream inputStream) {
        byte[] digest = null;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(inputStream);
            int theByte = 0;
            byte[] buffer = new byte[1024];
            while ((theByte = in.read(buffer)) != -1) {
                md5.update(buffer, 0, theByte);
            }
            digest = md5.digest();
        } catch (Exception e) {
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception e) {
                }
        }
        return ByteConvertor.bytesToHexString(digest);
    }

    private static byte[] MD5(File file) {
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));

            int theByte = 0;
            byte[] buffer = new byte[1024];
            while ((theByte = in.read(buffer)) != -1) {
                md5.update(buffer, 0, theByte);
            }
            in.close();

            return md5.digest();
        } catch (Exception e) {
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (Exception e) {
                }
        }

        return null;
    }

    /** 计算给定 byte [] 串的 MD5 */
    public static byte[] MD5(byte[] input) {
        if (md5 != null) {
            md5.update(input);
            return md5.digest();
        } else
            return null;
    }

    private static String AES_KEY = "huajiao";
    // 创建Key gen
    // private static KeyGenerator keyGenerator;
    // private static Cipher cipher;
    // private static SecretKeySpec key;
    static {
        AES_KEY = AES_KEY + "live";
        AES_KEY = AES_KEY.substring(5, AES_KEY.length() - 5);
        // try {
        // keyGenerator = KeyGenerator.getInstance("AES");
        // keyGenerator.init(128, new
        // SecureRandom(AES_KEY.getBytes())/*SecureRandom.getInstance("SHA1PRNG",
        // "Crypto")*/);
        // SecretKey secretKey = keyGenerator.generateKey();
        // byte[] codeFormat = secretKey.getEncoded();
        // key = new SecretKeySpec(codeFormat, "AES");
        //
        // cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // // cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new
        // byte[cipher.getBlockSize()]));
        // // cipher = Cipher.getInstance("AES/ECB/NoPadding");
        // // 初始化
        // // cipher.init(cipherMode, key);
        // } catch (NoSuchAlgorithmException e) {
        // e.printStackTrace();
        // } catch (NoSuchPaddingException e) {
        // e.printStackTrace();
        // }

    }

    /**
     * 对文件进行AES加密
     *
     * @param sourceFile
     * @param fileType
     * @return
     */
    public static File encryptFile(File sourceFile, String fileType) {
        // 新建临时加密文件
        File encrypfile = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        StringBuffer buf = new StringBuffer();
        try {
            inputStream = new FileInputStream(sourceFile);
            encrypfile = new File(sourceFile + fileType);
            outputStream = new FileOutputStream(encrypfile);
            byte[] cache = new byte[64];
            int nRead = 0;
            while ((nRead = inputStream.read(cache)) != -1) {
                buf.append(new String(cache, 0, nRead, "utf-8"));
            }
            String s = DES_encrypt(buf.toString());

            outputStream.write(s.getBytes("utf-8"));
            outputStream.flush();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                                     // File | Settings | File Templates.
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                                     // File | Settings | File Templates.
            }
        }
        return encrypfile;
    }

    /**
     * AES方式解密文件
     *
     * @param inputStream
     * @return
     */
    public static String decryptFile(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }

        String decryptString = null;
        StringBuffer buf = new StringBuffer();
        try {
            byte[] buffer = new byte[64];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                buf.append(new String(buffer, 0, r, "utf-8"));
            }
            decryptString = DES_decrypt(buf.toString()).trim();
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use File |
                                 // Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace(); // To change body of catch statement use
                                     // File | Settings | File Templates.
            }
        }
        return decryptString;
    }

    // /**
    // * 加密
    // *
    // * @param content
    // * 需要加密的内容
    // * @param password
    // * 加密密码
    // * @return
    // */
    // public static String encrypt(String content) {
    // try {
    // byte[] byteContent = content.getBytes("utf-8");
    // cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
    // byte[] result = cipher.doFinal(byteContent);
    // return ByteConvertor.bytesToHexString(result); // 加密
    // } catch (InvalidKeyException e) {
    // e.printStackTrace();
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // } catch (IllegalBlockSizeException e) {
    // e.printStackTrace();
    // } catch (BadPaddingException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    // /**
    // * 解密
    // *
    // * @param content
    // * 待解密内容
    // * @param password
    // * 解密密钥
    // * @return
    // */
    // public static byte[] decrypt(byte[] content, String password) {
    // try {
    // KeyGenerator kgen = KeyGenerator.getInstance("AES");
    // kgen.init(128, new SecureRandom(password.getBytes()));
    // SecretKey secretKey = kgen.generateKey();
    // byte[] enCodeFormat = secretKey.getEncoded();
    // SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
    // Cipher cipher = Cipher.getInstance("AES");// 创建密码器
    // cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
    // byte[] result = cipher.doFinal(content);
    // return result; // 加密
    // } catch (NoSuchAlgorithmException e) {
    // e.printStackTrace();
    // } catch (NoSuchPaddingException e) {
    // e.printStackTrace();
    // } catch (InvalidKeyException e) {
    // e.printStackTrace();
    // } catch (IllegalBlockSizeException e) {
    // e.printStackTrace();
    // } catch (BadPaddingException e) {
    // e.printStackTrace();
    // }
    // return null;
    // }

    public static String DES_encrypt(String plain) {
        try {

            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            // 由于 DES 要求秘钥是 64bit 的，而用户直接输入的 key 可能长度不够，这里简单点，先对 key 进行 md5，截断取前
            // 8 个字节
            DESKeySpec dks = new DESKeySpec(MD5(AES_KEY.getBytes()));

            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

            // 执行加密操作
            byte encryptedData[] = cipher.doFinal(plain.getBytes());

            return ByteConvertor.bytesToHexString(encryptedData);

        } catch (Exception e) {
            // e.printStackTrace();
        }

        return "";
    }

    public static String DES_decrypt(String encrypted) {
        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            // 由于 DES 要求秘钥是 64bit 的，而用户直接输入的 key 可能长度不够，这里简单点，先对 key 进行 md5，截断取前
            // 8 个字节
            DESKeySpec dks = new DESKeySpec(MD5(AES_KEY.getBytes()));

            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            // using DES in ECB mode
            Cipher cipher = Cipher.getInstance("DES");

            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

            // 正式执行解密操作
            byte decryptedData[] = cipher.doFinal(ByteConvertor.hexStringToBytes(encrypted));

            return new String(decryptedData);

        } catch (Exception e) {
            // e.printStackTrace();
            // 如果口令错误，解密失败，就会抛出异常
        }

        return "";
    }

    /**
     * byte十六进制转化
     *
     * @author zhangjinyuan
     */
    static class ByteConvertor {
        // String toHex(byte[] buf) {
        // if (buf == null)
        // return "";
        // StringBuffer result = new StringBuffer(2 * buf.length);
        // for (int i = 0; i < buf.length; i++) {
        // appendHex(result, buf[i]);
        // }
        // return result.toString();
        // }

        // private final static String HEX = "0123456789ABCDEF";
        //
        // private void appendHex(StringBuffer sb, byte b) {
        // // bigEnd sb.append(HEX.charAt(b & 0x0f)).append(HEX.charAt((b >> 4)
        // // &
        // // 0x0f));
        // sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        // }

        private final static String table = "0123456789abcdef";

        int toInt(byte[] byteArray4) {
            int intValue = 0;
            intValue |= (int) (byteArray4[3] & 0xff);
            intValue <<= 8;
            intValue |= (int) (byteArray4[2] & 0xff);
            intValue <<= 8;
            intValue |= (int) (byteArray4[1] & 0xff);
            intValue <<= 8;
            intValue |= (int) (byteArray4[0] & 0xff);
            return intValue;
        }

        long toLong(byte[] byteArray8) {
            long longValue = 0;
            longValue |= (long) (byteArray8[7] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[6] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[5] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[4] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[3] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[2] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[1] & 0xff);
            longValue <<= 8;
            longValue |= (long) (byteArray8[0] & 0xff);
            return longValue;
        }

        byte[] toBytes(int intValue) {
            byte[] byteValue = new byte[4];
            byteValue[0] = (byte) (intValue & 0xff);
            byteValue[1] = (byte) ((intValue & 0xff00) >> 8);
            byteValue[2] = (byte) ((intValue & 0xff0000) >> 16);
            byteValue[3] = (byte) ((intValue & 0xff000000) >> 24);
            return byteValue;
        }

        byte[] toBytes(long longValue) {
            byte[] byteValue = new byte[8];
            byteValue[0] = (byte) (longValue & 0xffl);
            byteValue[1] = (byte) ((longValue & 0xff00l) >> 8);
            byteValue[2] = (byte) ((longValue & 0xff0000l) >> 16);
            byteValue[3] = (byte) ((longValue & 0xff000000l) >> 24);
            byteValue[4] = (byte) ((longValue & 0xff00000000l) >> 32);
            byteValue[5] = (byte) ((longValue & 0xff0000000000l) >> 40);
            byteValue[6] = (byte) ((longValue & 0xff000000000000l) >> 48);
            byteValue[7] = (byte) ((longValue & 0xff00000000000000l) >> 56);
            return byteValue;
        }

        byte[] subBytes(byte[] buf, int from, int len) {
            byte[] subBuf = new byte[len];
            for (int i = 0; i < len; i++) {
                subBuf[i] = buf[from + i];
            }
            return subBuf;
        }

        /**
         * Converts a byte array into a String hexidecimal characters null
         * returns null
         */
        static String bytesToHexString(byte[] bytes) {
            if (bytes == null)
                return null;

            StringBuilder ret = new StringBuilder(2 * bytes.length);

            for (int i = 0; i < bytes.length; i++) {
                int b;
                b = 0x0f & (bytes[i] >> 4);
                ret.append(table.charAt(b));
                b = 0x0f & bytes[i];
                ret.append(table.charAt(b));
            }

            return ret.toString();
        }

        /**
         * Converts a hex String to a byte array.
         *
         * @param s A string of hexadecimal characters, must be an even number
         *            of chars long
         * @return byte array representation
         * @throws RuntimeException on invalid format
         */
        static byte[] hexStringToBytes(String s) {
            byte[] ret;

            if (s == null)
                return null;

            int sz = s.length();
            ret = new byte[sz / 2];
            for (int i = 0; i < sz; i += 2) {
                ret[i / 2] = (byte) ((hexCharToInt(s.charAt(i)) << 4) | hexCharToInt(s.charAt(i + 1)));
            }

            return ret;
        }

        static int hexCharToInt(char c) {
            if (c >= '0' && c <= '9')
                return (c - '0');
            if (c >= 'A' && c <= 'F')
                return (c - 'A' + 10);
            if (c >= 'a' && c <= 'f')
                return (c - 'a' + 10);

            throw new RuntimeException("invalid hex char '" + c + "'");
        }
    }
}
