package com.kite.crypto;

import com.kite.config.AppConfig;
import com.kite.utils.CryptoUtil;
import com.kite.utils.FileUtil;
import com.kite.utils.PrintUtil;
import org.bouncycastle.crypto.macs.CMac;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Encryptor Manager
 *
 * @author Calendar
 */
public class EncryptorManager {

    public void encrypt(String inputFilePath, String password, String passwordHint) throws Exception {
        byte[] salt = new byte[AppConfig.SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        SecretKey secretKey = CryptoUtil.generateSecretKey(password, salt);

        // IV
        byte[] ivExt = generateIV(AppConfig.IV_LENGTH_12); // for file extension
        byte[] ivFile = generateIV(AppConfig.IV_LENGTH_16); // for file content

        // Original file extension
        String fileExtension = FileUtil.getFileExtension(inputFilePath);
        byte[] encryptedExtension = encryptFileExtension(fileExtension, secretKey, ivExt);

        // Password hint
        byte[] encryptHint = Base64.getEncoder().encode(passwordHint.getBytes());

        // Compressing files while encrypting
        boolean isCompress = FileUtil.checkCompressFile(fileExtension);

        // eg. inputFilePath=/path/file.txt
        // outputFilePath = /path/file.kite
        String outputFilePath = FileUtil.replaceExtension(inputFilePath, AppConfig.ENCRYPT_EXTENSION);

        // Write header fields
        try (FileOutputStream out = new FileOutputStream(outputFilePath)) {
            out.write(AppConfig.KITE_FILE_MAGIC.getBytes());
            out.write(salt);
            out.write((byte) 1 & 0xFF); // program version
            out.write((byte) (isCompress ? 1:0) & 0xFF);
            out.write(new byte[4]); // reserved
            out.write((byte) encryptedExtension.length & 0xFF);
            out.write(encryptedExtension);
            out.write((byte) encryptHint.length & 0xFF);
            out.write(encryptHint);
            out.write(ivExt);
            out.write(ivFile);
        }

        // Write file data
        encryptFile(inputFilePath, outputFilePath, ivFile, secretKey, isCompress);
    }

    private void encryptFile(String inputFilePath, String outputFilePath, byte[] iv, SecretKey secretKey, boolean isCompress) throws Exception {
        // Start encrypting..
        System.out.println("Start encrypting..");

        // Encrypt and compress file content
        Cipher cipherFile = CryptoUtil.initCipher(Cipher.ENCRYPT_MODE, secretKey, iv, AppConfig.ALGORITHM_AES_CTR);

        try (FileOutputStream out = new FileOutputStream(outputFilePath, true);
             FileInputStream in = new FileInputStream(inputFilePath);
             BufferedInputStream bufferedIn = new BufferedInputStream(in)) {

            KiteOutputStream kiteOut = new KiteOutputStream(out, cipherFile, isCompress);

            // Initialize CMac
            CMac mac = CryptoUtil.initMac(secretKey);

            int totalBytes = in.available() - AppConfig.CMAC_LENGTH;
            int totalProcessBytes = 0;

            byte[] buffer = new byte[AppConfig.BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = bufferedIn.read(buffer)) != -1) {
                kiteOut.write(buffer, 0, bytesRead);
                mac.update(buffer, 0, bytesRead);

                PrintUtil.printProcessing(totalBytes, totalProcessBytes += bytesRead);
            }


            kiteOut.finish();

            // Write CMAC
            byte[] cmacOutput = new byte[AppConfig.CMAC_LENGTH];
            mac.doFinal(cmacOutput, 0);
            out.write(cmacOutput);

            PrintUtil.pink("\rProgressing.. 100%\33[?25h");
            PrintUtil.defaultColor("\nEncrypted file path: ");
            PrintUtil.blue(outputFilePath);
            PrintUtil.green("\nENCRYPTION SUCCESS");
            PrintUtil.defaultColor("\nFinished.\n\n");
        } catch (FileNotFoundException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("File not found.\n\n");
        }
    }

    private byte[] encryptFileExtension(String extension, SecretKey secretKey, byte[] iv) throws Exception {
        // Encrypt file extension
        Cipher cipherExt = CryptoUtil.initCipher(Cipher.ENCRYPT_MODE, secretKey, iv, AppConfig.ALGORITHM_AES_GCM);
        return cipherExt.doFinal(extension.getBytes(StandardCharsets.UTF_8));
    }

    private byte[] generateIV(int size) {
        byte[] iv = new byte[size];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
