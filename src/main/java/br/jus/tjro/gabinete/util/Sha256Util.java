package br.jus.tjro.gabinete.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Sha256Util {


    public static String getSha(byte[] input) throws NoSuchAlgorithmException, IOException {
        return getSha(new ByteArrayInputStream(input));

    }

    public static String getSha(InputStream input) throws NoSuchAlgorithmException, IOException {
        MessageDigest sha2 = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int len = input.read(buffer);
        while (len != -1) {
            sha2.update(buffer, 0, len);
            len = input.read(buffer);
        }
        return String.format("%032X", new BigInteger(1, sha2.digest()));
    }

    public static String sha256(String base) throws Exception {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new Exception(ex);
        }
    }
}
