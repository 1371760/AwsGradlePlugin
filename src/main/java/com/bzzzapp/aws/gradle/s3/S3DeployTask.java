package com.bzzzapp.aws.gradle.s3;

import com.bzzzapp.aws.gradle.AwsExtension;
import com.bzzzapp.aws.gradle.eb.EBExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class S3DeployTask extends DefaultTask {

    public static final String GRADLE_NAME = "awsS3Deploy";

    @TaskAction
    public void awsS3Deploy() {
        S3Extension awss3Config = getExtensions().getByType(S3Extension.class);
        AwsExtension awsConfig = getProject().getExtensions().getByType(AwsExtension.class);
        S3Deployer deployer = new S3Deployer(awsConfig.getAwsAccessKeyId(), awsConfig.getAwsSecretSharedKey(),
                awss3Config.getBucketName(), awss3Config.getPathToUpload());
        deployer.perform();
    }
}
