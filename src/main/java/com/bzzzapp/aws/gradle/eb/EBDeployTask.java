package com.bzzzapp.aws.gradle.eb;

import com.bzzzapp.aws.gradle.AwsExtension;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public class EBDeployTask extends DefaultTask {

    public static final String GRADLE_NAME = "awsEBDeploy";

    @TaskAction
    public void awsEBDeploy() throws Exception {
        EBExtension awsebConfig = getExtensions().getByType(EBExtension.class);
        AwsExtension awsConfig = getProject().getExtensions().getByType(AwsExtension.class);
        EBDeployer deployer = new EBDeployer(
                awsConfig.getAwsAccessKeyId(),
                awsConfig.getAwsSecretSharedKey(),
                awsebConfig.getAwsRegion(),
                awsebConfig.getApplicationName(),
                awsebConfig.getEnvironmentName(),
                awsebConfig.getBucketName(),
                awsebConfig.getBucketFolder(),
                awsebConfig.getVersionLabel(),
                awsebConfig.getWarFilePath()
        );
        deployer.perform();

    }
}
