package com.kite.uuid;

import com.kite.config.AppConfig;

import java.util.UUID;

/**
 * UUID Generator
 *
 * @author Calendar
 */
public class UUIDGenerator {

    public static void generate(String[] parameters) {
        int uuidCount = AppConfig.UUID_COUNT_DEFAULT;
        if(parameters.length > 1) {
            String sCount = parameters[1];
            if(sCount != null && sCount.matches("\\d{1,4}")) {
                uuidCount = Integer.parseInt(sCount);
            }
        }

        if(uuidCount < AppConfig.UUID_COUNT_MIN) {
            uuidCount = AppConfig.UUID_COUNT_MIN;
        } else if(uuidCount > AppConfig.UUID_COUNT_MAX) {
            uuidCount = AppConfig.UUID_COUNT_MAX;
        }

        for(int i = 0; i < uuidCount; i++) {
            System.out.println(UUID.randomUUID());
        }

        System.out.println();
        System.out.println("COUNT: " + uuidCount);
        System.out.println();
        System.exit(0);
    }
}
