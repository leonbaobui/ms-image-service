package com.twitter.image.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AmazonS3LocalConfig {
    @Value("${amazon.aws.access-key}")
    private String awsAccessKey;

    @Value("${amazon.aws.secret-key}")
    private String awsAccessSecret;

    @Bean
    @Profile({"dev", "local", "sit"})
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsAccessSecret);
        AwsClientBuilder.EndpointConfiguration endpoint =
                new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", Regions.EU_CENTRAL_1.getName());
        return AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .enablePathStyleAccess()
                .build();
    }
}
