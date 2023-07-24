package com.chunlei.bili.video.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

    @Value("endpoint")
    private String endpoint;

    @Value("access-key")
    private String accessKey;

    @Value("access-secret")
    private String accessSecret;

    @Value("bucket")
    private String bucket;

    @Bean(name = "amazonS3Client")
    public AmazonS3 amazonS3Client(){
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocol(Protocol.HTTP);
        config.setConnectionTimeout(5000);
        config.setUseExpectContinue(true);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);
        //设置Endpoint
        AwsClientBuilder.EndpointConfiguration end_point = new AwsClientBuilder.EndpointConfiguration(endpoint, Regions.CN_NORTH_1.getName());
        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withClientConfiguration(config)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(end_point)
                .withPathStyleAccessEnabled(true).build();
        return amazonS3;
    }
}
