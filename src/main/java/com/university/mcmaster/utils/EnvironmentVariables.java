package com.university.mcmaster.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvironmentVariables {
    public static final String ENVIRONMENT = getEnvironment();
    public static final String PROJECT_ID = getEnvString("PROJECT_ID",false);
    public static final String PUBSUB_EMULATOR_HOST = getEnvString("PUBSUB_EMULATOR_HOST",false);
    public static final String BUCKET_NAME = getEnvString("BUCKET_NAME",false);
    public static final String GOOGLE_APPLICATION_CREDENTIALS = getGoogleCreds();
    public static final String ADMIN_EMAIL = getEnvString("ADMIN_EMAIL",false);
    public static final String STRIPE_API_KEY = getEnvString("STRIPE_API_KEY",false);
    public static final String STRIPE_ENDPOINT_SECRET = getEnvString("STRIPE_ENDPOINT_SECRET",false);

    public static boolean isProd() {
        return ENVIRONMENT != null && ENVIRONMENT.contains("prod");
    }

    public static boolean isDev() {
        return ENVIRONMENT != null && ENVIRONMENT.contains("dev");
    }

    private static String getEnvironment() {
        String environment = getEnvString("ENVIRONMENT", false);
        if (environment == null) {
            environment = "env-prod";
        }
        return environment;
    }
    private static String getGoogleCreds() {
        String name = "GOOGLE_APPLICATION_CREDENTIALS";
        return getEnvString(name,true);
    }

    private static String getEnvString(String varName, boolean isSecret) {
        try {
            String tempVar = System.getenv(varName);
            return sanitiseString(tempVar);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return "";
    }

    private static String sanitiseString(String tempVar) {
        if (tempVar != null) {
            return tempVar.trim();
        }
        return tempVar;
    }
}
