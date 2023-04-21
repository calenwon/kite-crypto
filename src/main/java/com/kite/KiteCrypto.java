package com.kite;

import com.kite.base64.KiteBase64;
import com.kite.config.ConfigFileInfo;
import com.kite.crypto.DecryptManager;
import com.kite.crypto.EncryptorManager;
import com.kite.password.PasswordGenerator;
import com.kite.utils.ConfigFileUtil;
import com.kite.utils.PrintUtil;
import com.kite.utils.StringUtil;
import com.kite.uuid.UUIDGenerator;

/**
 * kite-crypto
 *
 * @author Calendar
 */
public class KiteCrypto {
    public static void main(String[] args) throws Exception {
        PrintUtil.printBanner();

        // Toolkit first
        if(args.length > 0) {
            toolkit(args);
        }

        // Parse configuration file
        ConfigFileInfo configFileInfo = new ConfigFileUtil().load(args.length > 0 ? args[0] : "");

        String inputFilePath = configFileInfo.getFilePath();
        String password = configFileInfo.getPassword();
        String passwordHint = configFileInfo.getHint();

        ConfigFileUtil.checkFilePathField(inputFilePath);
        ConfigFileUtil.checkPasswordField(password);

        boolean isKiteFile = DecryptManager.checkKiteFile(inputFilePath);
        ConfigFileUtil.checkPasswordHintField(passwordHint, !isKiteFile);

        if(isKiteFile) {
            new DecryptManager().decrypt(inputFilePath, password);
        } else {
            new EncryptorManager().encrypt(inputFilePath, password, passwordHint);
        }
    }

    private static void toolkit(String[] args) {
        String type = args[0];

        // password
        if(StringUtil.equalsIgnoreCase("-p", type)) {
            PasswordGenerator.generate(args);
        }
        // UUID
        else if(StringUtil.equalsIgnoreCase("-u", type)) {
            UUIDGenerator.generate(args);
        }
        // BASE64
        else if(StringUtil.equalsIgnoreCase("-be", type) || StringUtil.equalsIgnoreCase("-bd", type)) {
            KiteBase64.process(type, args);
        }

        if(type.startsWith("-")) {
            PrintUtil.printTookitHelp();
        }
    }
}