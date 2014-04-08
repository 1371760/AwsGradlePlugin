package com.bzzzapp.aws.gradle;

import com.bzzzapp.aws.gradle.eb.EBExtension;
import com.bzzzapp.aws.gradle.eb.EBDeployTask;
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
    }
}
