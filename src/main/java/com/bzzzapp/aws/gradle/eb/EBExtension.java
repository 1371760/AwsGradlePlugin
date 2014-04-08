package com.bzzzapp.aws.gradle.eb;

public class EBExtension {

    public static final String GRADLE_NAME = "awsEBConfig";

    /**
     * AWS Region
     */
    private String awsRegion;

    public String getAwsRegion() {
        return awsRegion;
    }

    public void setAwsRegion(String awsRegion) {
        this.awsRegion = awsRegion;
    }

    /**
     * Application Name
     */
    private String applicationName;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Environment Name
     */
    private String environmentName;

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    /**
     * Bucket Name
     */
    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * Key Format
     */
    private String keyPrefix;

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyFormat) {
        this.keyPrefix = keyFormat;
    }

    private String versionLabel;

    public String getVersionLabel() {
        return versionLabel;
    }

    public void setVersionLabel(String versionLabel) {
        this.versionLabel = versionLabel;
    }

    private String rootObject;

    public String getRootObject() {
        return rootObject;
    }

    public void setRootObject(String rootDirectory) {
        this.rootObject = rootDirectory;
    }
}
