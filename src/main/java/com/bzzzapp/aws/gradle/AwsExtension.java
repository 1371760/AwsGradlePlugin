package com.bzzzapp.aws.gradle;

public class AwsExtension {

    public static final String GRADLE_NAME = "awsConfig";

    /**
     * Access Key Id
     */
    private String awsAccessKeyId;

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    /**
     * Secret Shared Key
     */
    private String awsSecretSharedKey;

    public String getAwsSecretSharedKey() {
        return awsSecretSharedKey;
    }

    public void setAwsSecretSharedKey(String awsSecretSharedKey) {
        this.awsSecretSharedKey = awsSecretSharedKey;
    }
}
