package com.university.mcmaster.utils;


import com.google.cloud.storage.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import java.net.URL;

public class GcpStorageUtil {

    public static URL createGetUrl(String filePath) {
        Storage storage = StorageOptions.newBuilder().setProjectId(EnvironmentVariables.PROJECT_ID).build().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(EnvironmentVariables.BUCKET_NAME, filePath)).build();
        return storage.signUrl(blobInfo, 15, TimeUnit.MINUTES,Storage.SignUrlOption.httpMethod(HttpMethod.GET),
                Storage.SignUrlOption.withV4Signature());
    }

    public static String getCountries() {
        URL url = createGetUrl("static/countries.json");
        return new String(Utility.getData(url).toByteArray());
    }

    public static URL createPostUrl(String filePath, String contentType) {
        Storage storage = StorageOptions.newBuilder().setProjectId(EnvironmentVariables.PROJECT_ID).build().getService();
        BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(EnvironmentVariables.BUCKET_NAME, filePath)).build();
        Map<String, String> extensionHeaders = new HashMap<>();
        extensionHeaders.put("Content-Type", contentType);
        URL url = storage.signUrl(
                        blobInfo,
                        15,
                        TimeUnit.MINUTES,
                        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                        Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                        Storage.SignUrlOption.withV4Signature());
        return url;
    }
}
