package com.kite.crypto;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * KiteOutputStream
 *
 * @author Calendar
 */
public class KiteOutputStream {

    private GZIPOutputStream gzipOut;

    private CipherOutputStream cipherOut;

    public KiteOutputStream(FileOutputStream out, Cipher cipher, boolean isCompress) throws IOException {
        if(isCompress) {
            gzipOut = new GZIPOutputStream(new CipherOutputStream(out, cipher));
        } else {
            cipherOut = new CipherOutputStream(out, cipher);
        }
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        if(gzipOut != null) {
            gzipOut.write(buf, off, len);
        } else {
            cipherOut.write(buf, off, len);
        }
    }

    public void finish() throws IOException {
        if(gzipOut != null) {
            gzipOut.finish();
        }
    }

}
