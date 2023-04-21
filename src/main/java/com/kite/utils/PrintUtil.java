package com.kite.utils;

import com.kite.config.AppConfig;
import com.kite.enums.ColorEnum;

import java.time.LocalDateTime;

/**
 * Print tools
 *
 * @author Calendar
 */
public class PrintUtil {

    public static void defaultColor(String content) {
        println(content, ColorEnum.DEFAULT);
    }

    public static void red(String content) {
        println(content, ColorEnum.RED);
    }

    public static void green(String content) {
        println(content, ColorEnum.GREEN);
    }

    public static void yellow(String content) {
        println(content, ColorEnum.YELLOW);
    }

    public static void blue(String content) {
        println(content, ColorEnum.BLUE);
    }

    public static void pink(String content) {
        println(content, ColorEnum.PINK);
    }

    public static void cyan(String content) {
        println(content, ColorEnum.CYAN);
    }

    public static void printBanner() {
        System.out.println();
        System.out.println("----------------< " + ColorEnum.CYAN.getValue() + "kite-crypto" + ColorEnum.DEFAULT.getValue() + " >----------------");
        System.out.println("kite-crypto " + AppConfig.CURRENT_VERSION);
        System.out.println("Github: https://github.com/calenwon/kite-crypto");
        System.out.println("-----------------------------------------------\n");
    }

    public static void printTookitHelp() {
        System.out.println("Tookit Arguments:");
        System.out.println();
        System.out.println("  -p [length]\tGenerate password.");
        System.out.println("\t\t<length> is the length of the password, Default 20.");
        System.out.println();
        System.out.println("  -u [count]\tGenerate UUID.");
        System.out.println("\t\t<count> is the number of generated UUID, Default 20.");
        System.out.println();
        System.out.println("  -be <text>\tEncode <text> in base64.");
        System.out.println();
        System.out.println("  -bd <text>\tDecode <text> in base64.");
        System.out.println();
        System.exit(0);
    }

    public static void error() {
        System.out.print("[" + ColorEnum.RED.getValue() + "ERROR" + ColorEnum.DEFAULT.getValue() + "] ");
    }

    public static void printProcessing(long totalBytes, long totalProcessBytes) {
        if(LocalDateTime.now().getSecond() % 2 == 0) {
            if(totalBytes > 100) {
                pink("\rProgressing.. " + (totalProcessBytes / (totalBytes / 100)) + "% \33[?25l");
            }
        }
    }

    private static void println(String content, ColorEnum colorEnum) {
        System.out.print(colorEnum.getValue() + content + ColorEnum.DEFAULT.getValue());
    }

}