package com.twitter.image.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URI;

import static com.amazonaws.regions.Regions.EU_CENTRAL_1;

@Configuration
public class AmazonS3Config {
    @Value("${amazon.aws.access-key}")
    private String awsAccessKey;

    @Value("${amazon.aws.secret-key}")
    private String awsAccessSecret;

    @Value("${amazon.s3.url}")
    private String minioUrl;

   @Bean
   @Profile({"prod"})
   public AmazonS3 amazonS3() {
       AWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsAccessSecret);
       return AmazonS3ClientBuilder.standard()
               .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                       minioUrl, // Your MinIO service URL
                       EU_CENTRAL_1.getName()))
               .withPathStyleAccessEnabled(true)
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .withRequestHandlers(new ETagS3Interceptor())
               .disableChunkedEncoding()
               .build();
   }

    public static void listS3Buckets(AmazonS3 s3Client) {
        for (Bucket bucket : s3Client.listBuckets()) {
            System.out.println(bucket.getName());
        }
    }
}
