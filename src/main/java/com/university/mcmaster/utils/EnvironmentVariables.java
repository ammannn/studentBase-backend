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
    public static final String STRIPE_API_KEY = getEnvString("STRIPE_API_KEY",true);
    public static final String STRIPE_ENDPOINT_SECRET = getEnvString("STRIPE_ENDPOINT_SECRET",true);
    public static final String PLATFORM_URL = getPlatformUrl();
    public static final String SHEERID_PROGRAM_ID = getEnvString("SHEERID_PROGRAM_ID",true);
    public static final String SHEERID_API_TOKEN = getEnvString("SHEERID_API_TOKEN",true);

    private static String getPlatformUrl(){
        String env = getEnvironment();
        if(null == env || env.trim().isEmpty() || env.contains("prod")) return "https://studentbase.co";
        return "http://localhost:4200";
    }
    private static String getEnvUrlString(String env) {
        String url = getEnvString(env,false);
        if(null != url) url = removeTrailingSlash(url);
        return url;
    }

    public static String removeTrailingSlash(String url) {
        if (url != null && url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

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
            if(null == tempVar && isSecret){
                tempVar = SecretManager.getSecret(varName);
            }
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
