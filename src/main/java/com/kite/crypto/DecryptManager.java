package com.kite.crypto;

import com.kite.config.AppConfig;
import com.kite.utils.CryptoUtil;
import com.kite.utils.FileUtil;
import com.kite.utils.PrintUtil;
import com.kite.utils.StringUtil;
import org.bouncycastle.crypto.macs.CMac;

import javax.crypto.AEADBadTagException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.ProviderException;
import java.util.Arrays;
import java.util.Base64;

/**
 * Decrypt Manager
 *
 * @author Calendar
 */
public class DecryptManager {

    public void decrypt(String inputFile, String password) throws Exception {
        // Start decrypting..
        System.out.println("Verifying file..");

        String passwordHint = StringUtil.EMPTY;
        try (FileInputStream in = new FileInputStream(inputFile)) {
            byte[] magicBytes = in.readNBytes(AppConfig.KITE_FILE_MAGIC.getBytes().length);
            if(!new String(magicBytes).equals(AppConfig.KITE_FILE_MAGIC)) {
                PrintUtil.error();
                PrintUtil.defaultColor("Invalid kite encryption file.\n\n");
                System.exit(0);
            }

            // Read salt
            byte[] salt = in.readNBytes(AppConfig.SALT_LENGTH);

            // Read custom fields
            in.read(); // program version
            int compressValue = in.read();
            in.readNBytes(4); // reserved

            // Read encrypted extension
            byte[] encryptedExtension = in.readNBytes(in.read());

            // Password hint
            passwordHint = new String(Base64.getDecoder().decode(in.readNBytes(in.read())));

            // Read file extension IV
            byte[] ivExt = in.readNBytes(AppConfig.IV_LENGTH_12);

            // Read file content IV
            byte[] ivFile = in.readNBytes(AppConfig.IV_LENGTH_16);

            // Decrypt file extension
            SecretKey secretKey = CryptoUtil.generateSecretKey(password, salt);
            String originalFileExtension = decryptFileExtension(encryptedExtension, secretKey, ivExt);

            String outputFilePath = FileUtil.replaceExtension(inputFile, originalFileExtension);

            // Decrypt file
            decryptFile(in, outputFilePath, secretKey, ivFile, (compressValue == 1));
        } catch (FileNotFoundException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("File not found.\n\n");
        } catch (ProviderException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Invalid encryption file.\n\n");
        } catch (AEADBadTagException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Wrong password.\n");
            if(!StringUtil.isEmpty(passwordHint)) {
                PrintUtil.yellow("Password Hint: " + passwordHint + "\n");
            }
            System.out.println();
        }
    }

    private void decryptFile(FileInputStream in, String outputFilePath, SecretKey secretKey, byte[] iv, boolean isCompress) throws Exception {

        try (FileOutputStream out = new FileOutputStream(outputFilePath)) {

            // Decrypt and decompress file content
            Cipher cipherFile = CryptoUtil.initCipher(Cipher.DECRYPT_MODE, secretKey, iv, AppConfig.ALGORITHM_AES_CTR);
            KiteInputStream kiteIn = new KiteInputStream(in, cipherFile, isCompress);

            // Start decrypting..
            System.out.println("Start decrypting..");

            // Initialize Mac
            CMac mac = CryptoUtil.initMac(secretKey);

            int totalBytes = in.available() - AppConfig.CMAC_LENGTH;
            int totalProcessBytes = 0;

            byte[] buffer = new byte[AppConfig.BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = kiteIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                mac.update(buffer, 0, bytesRead);

                PrintUtil.printProcessing(totalBytes, totalProcessBytes += bytesRead);
            }

            // Verify CMAC
            byte[] inputCmac = in.readAllBytes();
            byte[] calculatedCmac = new byte[AppConfig.CMAC_LENGTH];
            mac.doFinal(calculatedCmac, 0);
            if (!Arrays.equals(inputCmac, calculatedCmac)) {
                throw new SecurityException("Data integrity check failed. CMAC does not match.");
            }

            PrintUtil.pink("\rProgressing.. 100%\33[?25h");
            PrintUtil.defaultColor("\nDecrypted file path: ");
            PrintUtil.blue(outputFilePath);
            PrintUtil.green("\nDECRYPTION SUCCESS");
            PrintUtil.defaultColor("\nFinished.\n\n");
        } catch (EOFException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Invalid decryption file.\n\n");
        }
    }

    public static boolean checkKiteFile(String inputFilePath) {
        try (FileInputStream in = new FileInputStream(inputFilePath)) {
            byte[] magicBytes = in.readNBytes(AppConfig.KITE_FILE_MAGIC.getBytes().length);
            if (new String(magicBytes).equals(AppConfig.KITE_FILE_MAGIC)) {
                return true;
            }
        } catch (IOException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("An error occurred while reading the file.\n\n");
            System.exit(0);
        }

        return false;
    }

    private String decryptFileExtension(byte[] encryptedExtension, SecretKey secretKey, byte[] iv) throws Exception {
        // Decrypt file extension
        Cipher cipherExt = CryptoUtil.initCipher(Cipher.DECRYPT_MODE, secretKey, iv, AppConfig.ALGORITHM_AES_GCM);
        byte[] decryptedExtensionBytes = cipherExt.doFinal(encryptedExtension);
        return new String(decryptedExtensionBytes, StandardCharsets.UTF_8);
    }

}
