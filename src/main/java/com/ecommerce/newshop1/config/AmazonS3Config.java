//package com.ecommerce.newshop1.config;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3Client;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AmazonS3Config {
//
//    @Value("${cloud.aws.credentials.access-key}")
//    public String accessKey;
//
//    @Value("${cloud.aws.credentials.secret-key}")
//    public String secretKey;
//
//    @Value("${cloud.aws.region.static}")
//    public String region;
//
//
//    @Bean
//    public AmazonS3Client amazonS3Client(){
//        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
//        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
//                .withRegion(region)
//                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
//                .build();
//    }
//
//
//}
