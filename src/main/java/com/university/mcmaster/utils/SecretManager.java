package com.university.mcmaster.utils;

import com.google.protobuf.ByteString;

import java.util.TreeMap;
import com.google.cloud.secretmanager.v1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecretManager {
    private static final Logger log = LoggerFactory.getLogger(SecretManager.class);

    public static String getSecret(String id) {
        String payload = null;
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(EnvironmentVariables.PROJECT_ID, id, "latest");
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            payload = response.getPayload().getData().toStringUtf8();
            client.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return payload;
    }

    public static boolean addSecret(String varName,String value) {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            ProjectName projectName = ProjectName.of(EnvironmentVariables.PROJECT_ID);
            Secret secret = Secret.newBuilder()
                    .setReplication(
                            Replication.newBuilder()
                                    .setAutomatic(Replication.Automatic.newBuilder().build())
                                    .build())
                    .build();
            Secret createdSecret = client.createSecret(projectName, varName, secret);
            SecretPayload payload =
                    SecretPayload.newBuilder().setData(ByteString.copyFromUtf8(value)).build();
            SecretVersion addedVersion = client.addSecretVersion(createdSecret.getName(), payload);
            if (addedVersion.getStateValue() == 1 && addedVersion.isInitialized()) {
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    public static boolean deleteSecret(String id) {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(EnvironmentVariables.PROJECT_ID, id, "latest");
            client.deleteSecret(id);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
