package com.firstamerican.portal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${aws.region:us-east-1}")
    private String awsRegion;

    @Bean
    public S3Client s3Client() {
        // Automatically picks up credentials from EKS IAM Roles for Service Accounts (IRSA)
        return S3Client.builder()
                .region(Region.of(awsRegion))
                .build();
    }
}