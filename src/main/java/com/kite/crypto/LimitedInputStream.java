package com.kite.crypto;

import com.kite.config.AppConfig;

import java.io.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Determine the end point of the encrypted file.
 *
 * @author Calendar
 */
public class LimitedInputStream extends FilterInputStream {

    private final AtomicLong bytesRemaining;

    public LimitedInputStream(InputStream inputStream) throws IOException {
        super(inputStream);
        bytesRemaining = new AtomicLong(available() - AppConfig.CMAC_LENGTH);
    }

    @Override
    public int read(byte[] b) throws IOException {

        if (bytesRemaining.get() <= 0) {
            return -1;
        }

        int bytesRead = super.read(b, 0, Math.min(b.length, bytesRemaining.intValue()));
        if (bytesRead != -1) {
            bytesRemaining.addAndGet(bytesRead * -1);
        }

        return bytesRead;
    }

}