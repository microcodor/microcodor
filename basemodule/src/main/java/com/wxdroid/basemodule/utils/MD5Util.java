
package com.wxdroid.basemodule.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util
{

    private static final int STREAM_BUFFER_LENGTH = 1024;
    public static final String MD5 = "MD5";

    private static final char[] DIGITS_LOWER =
        {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };

    private static final char[] DIGITS_UPPER =
        {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };

    // private static final char HEX_DIG[] = { '0', '1', '2', '3', '4', '5',
    // '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
    // 'f' };

    public static String getMD5code(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();

            //          return buf.toString().substring(8,24);

        } catch (Exception e) {
            return "";
        }
    }

    public static String getMD5code(byte[] value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value);
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();

        } catch (Exception e) {
            return "";
        }
    }

    private static char[] encodeHex(final byte[] data, final char[] toDigits) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }

    private static char[] encodeHex(final byte[] data, final boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(final byte[] data) {
        return encodeHex(data, true);
    }

    private static String encodeHexString(final byte[] data) {
        return new String(encodeHex(data));
    }

    private static MessageDigest updateDigest(final MessageDigest digest, final InputStream data) throws IOException
    {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > -1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }

        return digest;
    }

    private static byte[] digest(final MessageDigest digest, final InputStream data) throws IOException
    {
        return updateDigest(digest, data).digest();
    }

    private static MessageDigest getMd5Digest() {
        return getDigest(MD5);
    }

    private static byte[] md5(final InputStream data) throws IOException
    {
        return digest(getMd5Digest(), data);
    }

    private static MessageDigest getDigest(final String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String md5Hex(final File file) throws IOException
    {
        if (file == null || !file.exists()) {
            return "";
        }
        return encodeHexString(md5(new FileInputStream(file)));
    }

    public static String md5Hex(final InputStream data) throws IOException
    {
        return encodeHexString(md5(data));
    }
}
