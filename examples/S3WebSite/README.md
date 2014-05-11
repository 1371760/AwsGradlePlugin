USAGE
=====

Fill in the build.gradle all aws credentials and s3 parameters. Then execute

    ./gradlew awsS3Deploy.

All content of S3WebSite inner folder will be uploaded to s3 bucket you specified.
All content of the bucket clears before uploading.