package com.kite.password;

import com.kite.config.AppConfig;
import com.kite.utils.PrintUtil;
import com.kite.utils.StringUtil;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Password Generator
 *
 * @author Calendar
 */
public class PasswordGenerator {
    private static final String NUMBERS = "0123456789";
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String SYMBOLS = "!@#$%^&*()-_=+[{]};:,<.>/?`~";

    private static final int NUMERIC = 1;
    private static final int NUMERIC_ALPHA = 2;
    private static final int NUMERIC_ALPHA_SYMBOLS = 3;

    public static void generate(String[] parameters) {
        int passwordLength = AppConfig.PASSWORD_LENGTH_DEFAULT;
        if(parameters.length > 1) {
            String sLength = parameters[1];
            if(sLength != null && sLength.matches("\\d{1,3}")) {
                passwordLength = Integer.parseInt(sLength);
            }
        }

        if(passwordLength < AppConfig.PASSWORD_LENGTH_MIN) {
            passwordLength = AppConfig.PASSWORD_LENGTH_MIN;
        } else if(passwordLength > AppConfig.PASSWORD_LENGTH_MAX) {
            passwordLength = AppConfig.PASSWORD_LENGTH_MAX;
        }

        // Numbers
        String numericPassword = PasswordGenerator.generate(passwordLength, NUMERIC);
        System.out.println("Numbers: \t" + numericPassword);

        // Numbers + ALPHA
        String alphanumericPassword = PasswordGenerator.generate(passwordLength, NUMERIC_ALPHA);
        System.out.println("Mixed: \t\t" + alphanumericPassword);

        // Numbers + ALPHA + SYMBOLS
        String alphanumericSpecialPassword = PasswordGenerator.generate(passwordLength, NUMERIC_ALPHA_SYMBOLS);
        System.out.println("Symbols: \t" + alphanumericSpecialPassword);

        System.out.println();
        System.out.println("Length: " + passwordLength);
        System.out.println();
        System.exit(0);
    }

    private static String generate(int length, int type) {
        String allowedCharacters = StringUtil.EMPTY;

        if (type == NUMERIC) {
            allowedCharacters = NUMBERS;
        } else if (type == NUMERIC_ALPHA) {
            allowedCharacters = NUMBERS + ALPHABET;
        } else if (type == NUMERIC_ALPHA_SYMBOLS) {
            allowedCharacters = NUMBERS + ALPHABET + SYMBOLS;
        }

        if (allowedCharacters.isEmpty()) {
            PrintUtil.error();
            PrintUtil.defaultColor("Invalid password type.\n\n");
            System.exit(0);
        }

        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            char randomChar = allowedCharacters.charAt(randomIndex);
            password.append(randomChar);
        }

        return password.toString();
    }
}
