package com.kite.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * KiteInputStream
 *
 * @author Calendar
 */
public class KiteInputStream {

    private GZIPInputStream gzipIn;

    private CipherInputStream cipherIn;

    public KiteInputStream(FileInputStream in, Cipher cipher, boolean decompress) throws IOException {
        if(decompress) {
            gzipIn = new GZIPInputStream(new CipherInputStream(new LimitedInputStream(in), cipher));
        } else {
            cipherIn = new CipherInputStream(new LimitedInputStream(in), cipher);
        }
    }

    public int read(byte[] b) throws IOException {
        if(gzipIn != null) {
            return gzipIn.read(b);
        } else {
            return cipherIn.read(b);
        }
    }
}
