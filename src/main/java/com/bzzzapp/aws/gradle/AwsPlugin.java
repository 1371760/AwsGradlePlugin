package com.bzzzapp.aws.gradle;

import com.bzzzapp.aws.gradle.eb.EBExtension;
import com.bzzzapp.aws.gradle.eb.EBDeployTask;
import com.bzzzapp.aws.gradle.s3.S3DeployTask;
import com.bzzzapp.aws.gradle.s3.S3Extension;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class AwsPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().add(AwsExtension.GRADLE_NAME, AwsExtension.class);

        project.getTasks()
                .create(EBDeployTask.GRADLE_NAME, EBDeployTask.class)
                .getExtensions()
                .add(EBExtension.GRADLE_NAME, EBExtension.class);

        project.getTasks()
                .create(S3DeployTask.GRADLE_NAME, S3DeployTask.class)
                .getExtensions()
                .add(S3Extension.GRADLE_NAME, S3Extension.class);
    }
}
