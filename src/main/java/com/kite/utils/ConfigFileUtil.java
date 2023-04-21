package com.kite.utils;

import com.kite.config.ConfigFileInfo;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration file tools
 *
 * @author Calendar
 */
public class ConfigFileUtil {

    private static final String CONFIG_FILE_NAME = "kite.conf";

    public ConfigFileInfo load(String configFilePath) {
        List<String> lines = readFile(configFilePath);

        ConfigFileInfo info = new ConfigFileInfo();
        for (String line : lines) {
            line = line.trim().replaceAll("\\s*=\\s*", "=");

            if(line.startsWith("password=")) {
                info.setPassword(line.replaceFirst("password=", ""));
            } else if(line.startsWith("filePath=")) {
                info.setFilePath(line.replaceFirst("filePath=", ""));
            } else if(line.startsWith("hint=")) {
                info.setHint(line.replaceFirst("hint=", ""));
            }
        }

        if(info.getHint() == null) {
            info.setHint(StringUtil.EMPTY);
        }

        return info;
    }

    private List<String> readFile(String filePath) {
        if(!StringUtil.isEmpty(filePath)) {
            File configFile = new File(filePath);
            if(!configFile.exists()) {
                PrintUtil.error();
                PrintUtil.defaultColor("The specified configuration file does not exist.\n\n");
                System.exit(1);
            }
        } else {
            filePath = getJarLocation() + File.separator + CONFIG_FILE_NAME;
            if(!new File(filePath).exists()) {
                autoCreateConfigFile(filePath);
                System.exit(0);
            }
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if(line.startsWith("#") || StringUtil.isEmpty(line.trim())) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Error reading configuration file.\n\n");
            System.exit(1);
        }

        return lines;
    }

    private void autoCreateConfigFile(String filePath) {
        String content = "# The password of your encrypted file, must be saved,\n" +
                "# if you forget it will never be able to decrypt the file.\n" +
                "password=your-password\n\n" +
                "# Path to the encrypted or decrypted file.\n" +
                "# The program automatically determines\n" +
                "# whether the file is encrypted or decrypted.\n" +
                "filePath=/path/file\n\n" +
                "# Password Hint\n" +
                "# Help you recall your password if you forget it.\n" +
                "# Optional, but recommended to turn it on and set it.\n" +
                "# This value is ignored when decrypting a file.\n" +
                "#hint=What is your cat's name?";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Error creating configuration file automatically.\n\n");
            System.exit(1);
        }

        PrintUtil.error();
        System.out.print("The ");
        PrintUtil.blue(CONFIG_FILE_NAME);
        System.out.println(" file is missing");
        PrintUtil.yellow("but has been automatically created for you, \nplease edit it and run your command again.\n");
        System.out.print("file path: ");
        PrintUtil.blue(filePath);
        System.out.println("\n");
    }

    private static String getJarLocation() {
        try {
            return Paths.get(ConfigFileUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().toString();
        } catch (URISyntaxException e) {
            PrintUtil.error();
            PrintUtil.defaultColor("Error getting jar file directory.\n\n");
            System.exit(1);
        }

        return StringUtil.EMPTY;
    }

    public static void checkFilePathField(String filePath) {
        if(StringUtil.isEmpty(filePath)) {
            PrintUtil.error();
            PrintUtil.defaultColor("The ");
            PrintUtil.blue("`filePath`");
            PrintUtil.defaultColor(" in the configuration file cannot be empty.\n\n");
            System.exit(0);
        }

        File file = new File(filePath);
        if(!file.exists()) {
            PrintUtil.error();
            PrintUtil.defaultColor("File not found.\n\n");
            System.exit(0);
        }

        if(file.isDirectory()) {
            PrintUtil.error();
            PrintUtil.defaultColor("Does not support encryption of a directory.\n");
            PrintUtil.yellow("You can package the directory first and then encrypt it.\n\n");
            System.exit(0);
        }
    }

    public static void checkPasswordField(String password) {
        if(StringUtil.isEmpty(password)) {
            PrintUtil.error();
            PrintUtil.defaultColor("The ");
            PrintUtil.blue("`password`");
            PrintUtil.defaultColor(" in the configuration file cannot be empty.\n\n");
            System.exit(0);
        }
    }

    public static void checkPasswordHintField(String passwordHint, boolean encrypt) {
        if(!StringUtil.isEmpty(passwordHint)) {
            if(encrypt && passwordHint.length() > 25) {
                PrintUtil.error();
                PrintUtil.defaultColor("The length of the ");
                PrintUtil.blue("`hint`");
                PrintUtil.defaultColor(" value in the \nconfiguration file cannot be more than 25 characters.\n\n");
                System.exit(0);
            }
        }
    }
}
