package com.kite.config;

/**
 * Application configuration
 *
 * @author Calendar
 */
public final class AppConfig {

    public static final int GCM_TAG_LENGTH = 128; // in bits
    public static final int BUFFER_SIZE = 8192; // 8 KB
    public static final int CMAC_LENGTH = 16; // byte
    public static final int IV_LENGTH_12 = 12;
    public static final int IV_LENGTH_16 = 16;
    public static final int SALT_LENGTH = 16;

    public static final String ALGORITHM_AES_GCM = "AES/GCM/NoPadding";

    public static final String ALGORITHM_AES_CTR = "AES/CTR/NoPadding";

    public static final String CURRENT_VERSION = "v0.1.0";

    public static final String ENCRYPT_EXTENSION = ".kite";

    public static final String KITE_FILE_MAGIC = "KITE";

    public static final int PASSWORD_LENGTH_DEFAULT = 20;
    public static final int PASSWORD_LENGTH_MAX = 100;
    public static final int PASSWORD_LENGTH_MIN = 4;
    public static final int UUID_COUNT_DEFAULT = 10;
    public static final int UUID_COUNT_MAX = 1000;
    public static final int UUID_COUNT_MIN = 1;
}
