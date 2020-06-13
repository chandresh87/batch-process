package com.cm.batch.download;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import io.findify.s3mock.S3Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author chandresh.mishra
 */
@Component
@Profile("dev")
public class MockFileDownloader implements FileDownloader {


    private final String s3BackEnd;
    private final String bucketName;
    private final Logger logger = LoggerFactory.getLogger(MockFileDownloader.class);

    public MockFileDownloader(@Value("${s3.local.file.path}") String s3BackEnd, @Value("${s3.local.bucketName}") String bucketName) {
        this.s3BackEnd = s3BackEnd;
        this.bucketName = bucketName;
    }

    @Override
    public String downloadFile() throws IOException {

        S3Mock api = new S3Mock.Builder().withPort(8001).withFileBackend(s3BackEnd).build();
        api.start();

        EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:8001", "us-west-2");
        AmazonS3 client =
                AmazonS3ClientBuilder
                        .standard()
                        .withPathStyleAccessEnabled(true)
                        .withEndpointConfiguration(endpoint)
                        .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
                        .build();

        File file = ResourceUtils.getFile("classpath:static/file1.txt");
        client.createBucket(bucketName);
        client.putObject(bucketName, "customerFile.txt", file);
        File downloadedFile = File.createTempFile("customerFile", ".txt");
        logger.info("temp file {}",  downloadedFile.getAbsolutePath());
        S3Object s3Object = client.getObject(bucketName, "customerFile.txt");
        InputStream objectData  = s3Object.getObjectContent();
        StreamUtils.copy(objectData,new FileOutputStream(downloadedFile));
        return downloadedFile.getAbsolutePath();
    }
}
