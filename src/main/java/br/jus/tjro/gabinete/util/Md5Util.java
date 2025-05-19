package br.jus.tjro.gabinete.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {


    public static String getMd5(byte[] input) throws NoSuchAlgorithmException, IOException {
        return getMd5(new ByteArrayInputStream(input));

    }

    public static String getMd5(InputStream input) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[8192];
        int len = input.read(buffer);
        while (len != -1) {
            md5.update(buffer, 0, len);
            len = input.read(buffer);
        }
        return String.format("%032X", new BigInteger(1, md5.digest()));
    }

}
