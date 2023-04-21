package com.kite.utils;

import com.kite.config.AppConfig;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.macs.CMac;
import org.bouncycastle.crypto.params.KeyParameter;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Crypto tools
 *
 * @author Calendar
 */
public class CryptoUtil {

    public static SecretKey generateSecretKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] secretKeyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(secretKeyBytes, "AES");
    }

    public static Cipher initCipher(int mode, SecretKey secretKey, byte[] iv, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        if (algorithm.equals(AppConfig.ALGORITHM_AES_GCM)) {
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(AppConfig.GCM_TAG_LENGTH, iv);
            cipher.init(mode, secretKey, gcmParameterSpec);
        } else {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(mode, secretKey, ivParameterSpec);
        }
        return cipher;
    }

    public static CMac initMac(SecretKey key) {
        CMac mac = new CMac(new AESEngine());
        mac.init(new KeyParameter(key.getEncoded()));
        return mac;
    }

}
