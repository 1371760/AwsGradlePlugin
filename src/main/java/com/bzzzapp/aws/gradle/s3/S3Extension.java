package com.bzzzapp.aws.gradle.s3;

public class S3Extension {

    public static final String GRADLE_NAME = "awsS3Config";

    private String bucketName;

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    private String pathToUpload;

    public String getPathToUpload() {
        return pathToUpload;
    }

    public void setPathToUpload(String pathToUpload) {
        this.pathToUpload = pathToUpload;
    }

}
