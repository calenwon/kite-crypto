package com.kite.base64;

import com.kite.utils.PrintUtil;
import com.kite.utils.StringUtil;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Base64 tools
 *
 * @author Calendar
 */
public class KiteBase64 {

    public static void process(String type, String[] parameters) {
        if(parameters.length != 2) {
            if(parameters.length == 1) {
                PrintUtil.error();
                PrintUtil.defaultColor("The text to be encoded is not found in the arguments.\n\n");
                System.exit(0);
            } else {
                PrintUtil.error();
                PrintUtil.defaultColor("If there are spaces in the text, the entire text \nneeds to be enclosed in double quotes(`\"`).\n\n");
                System.exit(0);
            }
        }

        String text = parameters[1];
        if(text.startsWith("\"")) {
            text = text.replaceFirst("\"", "");
        }
        if(text.endsWith("\"")) {
            text = text.substring(0, text.length() -1);
        }

        if(StringUtil.isEmpty(text)) {
            PrintUtil.error();
            PrintUtil.defaultColor("The encoded text cannot be empty.\n\n");
            System.exit(0);
        }

        if(type.endsWith("-be")) {
            base64Encode(text);
        } else {
            base64Decode(text);
        }

        System.exit(0);
    }

    // Base64 Encode
    private static void base64Encode(String text) {
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        System.out.println(Base64.getEncoder().encodeToString(textBytes));
        System.out.println();
        System.exit(0);
    }

    // Base64 Decode
    private static void base64Decode(String encodedText) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        System.out.println(new String(decodedBytes, StandardCharsets.UTF_8));
        System.out.println();
        System.exit(0);
    }

}
