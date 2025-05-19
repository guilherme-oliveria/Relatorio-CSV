package br.jus.tjro.gabinete.util.jcr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;

public class HashProvider {
    public static String ALGORITM = "SHA-1";
    private final MessageDigest digest;
    private final DigestInputStream dis;

    public HashProvider(InputStream input, String algoritm) {
        if (input == null) {
            throw new IllegalArgumentException("Parameter input can not be null");
        } else {
            try {
                this.digest = MessageDigest.getInstance(algoritm);
                this.dis = new DigestInputStream(input, this.digest);
            } catch (NoSuchAlgorithmException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public InputStream getInputStream() {
        if (this.dis == null) {
            throw new IllegalStateException("Null input stream");
        } else {
            return this.dis;
        }
    }

    public String hash() {
        return new String(Hex.encodeHex(this.digest.digest()));
    }

    public static String hash(byte[] barray) {
        return hash(barray, ALGORITM);
    }

    public static String hash(byte[] barray, String algoritm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algoritm);
            digest.reset();
            return new String(Hex.encodeHex(digest.digest(barray)));
        } catch (NoSuchAlgorithmException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String hash(InputStream stream) {
        return hash(stream, ALGORITM);
    }

    public static String hash(InputStream stream, String algoritm) {
        try {
            HashProvider provider = new HashProvider(stream, algoritm);
            IOUtils.copyLarge(provider.getInputStream(), new NullOutputStream());
            return provider.hash();
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    public static String hash(File file) {
        return hash(file, ALGORITM);
    }

    public static String hash(File file, String algoritm) {
        InputStream stream = null;

        String var3;
        try {
            stream = new FileInputStream(file);
            var3 = hash((InputStream)stream, algoritm);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        } finally {
            IOUtils.closeQuietly(stream);
        }

        return var3;
    }
}
