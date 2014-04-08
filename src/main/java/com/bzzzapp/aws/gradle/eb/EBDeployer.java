package com.bzzzapp.aws.gradle.eb;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalk;
import com.amazonaws.services.elasticbeanstalk.AWSElasticBeanstalkClient;
import com.amazonaws.services.elasticbeanstalk.model.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class EBDeployer {

    private static final int MAX_ATTEMPTS = 15;

    private AmazonS3 s3;

    private AWSElasticBeanstalk awseb;

    private String awsRegion;

    private String awsSecretSharedKey;

    private String awsAccessKeyId;

    private String versionLabel;

    private String rootObject;

    private File localArchive;

    private String keyPrefix;

    private String bucketName;

    private String applicationName;

    private String objectKey;

    private String s3ObjectPath;

    private String environmentName;

    public EBDeployer(String awsAccessKeyId,
                      String awsSecretSharedKey, String awsRegion,
                      String applicationName, String environmentName, String bucketName,
                      String keyPrefix, String versionLabel, String rootObject) {
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretSharedKey = awsSecretSharedKey;
        this.awsRegion = awsRegion;
        this.applicationName = applicationName;
        this.environmentName = environmentName;
        this.bucketName = bucketName;
        this.keyPrefix = keyPrefix;
        this.versionLabel = versionLabel;
        this.rootObject = rootObject;
    }

    public void perform() throws Exception {
        initAWS();

        localArchive = getLocalFileObject(rootObject);

        uploadArchive();

        createApplicationVersion();

        updateEnvironments();

    }

    private void updateEnvironments() throws Exception {
        DescribeEnvironmentsResult environments = awseb
                .describeEnvironments(new DescribeEnvironmentsRequest()
                        .withApplicationName(applicationName)
                        .withEnvironmentNames(environmentName));

        boolean found = (1 == environments.getEnvironments().size());

        if (found) {
            for (int nAttempt = 1; nAttempt <= MAX_ATTEMPTS; nAttempt++) {
                String environmentId = environments.getEnvironments().get(0)
                        .getEnvironmentId();


                UpdateEnvironmentRequest uavReq = new UpdateEnvironmentRequest()
                        .withEnvironmentName(environmentName).withVersionLabel(
                                versionLabel);

                try {
                    awseb.updateEnvironment(uavReq);

                    return;
                } catch (Exception exc) {

                    if (nAttempt == MAX_ATTEMPTS) {
                        throw exc;
                    }

                    Thread.sleep(TimeUnit.SECONDS.toMillis(90));
                }
            }
        } else {
            throw new UnsupportedOperationException("environment not found");
        }
    }

    private void createApplicationVersion() {
        CreateApplicationVersionRequest cavRequest = new CreateApplicationVersionRequest()
                .withApplicationName(applicationName)
                .withAutoCreateApplication(true)
                .withSourceBundle(new S3Location(bucketName, objectKey))
                .withVersionLabel(versionLabel);

        awseb.createApplicationVersion(cavRequest);
    }

    private void uploadArchive() {
        objectKey = formatPath("%s/%s-%s.war", keyPrefix, applicationName,
                versionLabel);

        s3ObjectPath = "s3://" + formatPath("%s/%s", bucketName, objectKey);

        s3.putObject(bucketName, objectKey, localArchive);
    }

    private void initAWS() {
        AWSCredentialsProvider credentials = new AWSCredentialsProviderChain(
                new StaticCredentialsProvider(new BasicAWSCredentials(
                        awsAccessKeyId,
                        awsSecretSharedKey)));
        Region region = Region.getRegion(Regions.fromName(awsRegion));
        ClientConfiguration clientConfig = new ClientConfiguration();

        clientConfig.setUserAgent("AwsGradlePlugin");

        s3 = region.createClient(AmazonS3Client.class, credentials,
                clientConfig);
        awseb = region.createClient(AWSElasticBeanstalkClient.class,
                credentials, clientConfig);
    }

    private File getLocalFileObject(String rootObject) {
        File resultFile = new File(rootObject);


        if (!resultFile.exists() || resultFile.isDirectory()) {
            throw new UnsupportedOperationException("root file must be existing war file");
        }

        return resultFile;
    }

    private String formatPath(String mask, Object... args) {
        return String.format(mask, args).replaceAll("/{2,}", "");
    }
}
