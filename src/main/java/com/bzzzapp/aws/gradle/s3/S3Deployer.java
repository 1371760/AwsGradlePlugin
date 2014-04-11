package com.bzzzapp.aws.gradle.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import org.apache.xerces.xs.datatypes.ObjectList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class S3Deployer {

    public static final String GRADLE_NAME = "awsS3Deploy";

    private final String awsAccessKeyId;
    private final String awsSecretSharedKey;
    private String bucketName;
    private String pathToUpload;

    private AmazonS3 s3;
    private TransferManager tm;

    public S3Deployer(String awsAccessKeyId,
                      String awsSecretSharedKey,
                      String bucketName,
                      String pathToUpload) {
        this.awsAccessKeyId = awsAccessKeyId;
        this.awsSecretSharedKey = awsSecretSharedKey;
        this.bucketName = bucketName;
        this.pathToUpload = pathToUpload;
    }

    public void perform() {
        initAWS();
        clearFolder();
        upload();
    }

    private void initAWS() {
        AWSCredentialsProvider credentials = new AWSCredentialsProviderChain(
                new StaticCredentialsProvider(new BasicAWSCredentials(
                        awsAccessKeyId,
                        awsSecretSharedKey)));
        ClientConfiguration clientConfig = new ClientConfiguration();

        clientConfig.setUserAgent("AwsGradlePlugin");

        s3 = new AmazonS3Client(credentials);
        tm = new TransferManager(s3);
    }

    private void clearFolder() {
        System.out.println("clear s3 bucket");
        ObjectListing objectListing = s3.listObjects(bucketName);
        if (objectListing.getObjectSummaries().size() > 0) {
            DeleteObjectsRequest deleteRequest = new DeleteObjectsRequest(bucketName);
            List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<DeleteObjectsRequest.KeyVersion>();
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                keys.add(new DeleteObjectsRequest.KeyVersion(objectSummary.getKey()));
            }
            deleteRequest.setKeys(keys);
            s3.deleteObjects(deleteRequest);
        }
    }

    private void upload() {
        System.out.println("uplaod to s3 bucket");
        File file = new File(pathToUpload);
//        s3.putObject(bucketName, file.getName(), file);
        MultipleFileUpload mfu = tm.uploadDirectory(bucketName, null, file, true);
        try {
            mfu.waitForCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tm.shutdownNow();
    }
}
