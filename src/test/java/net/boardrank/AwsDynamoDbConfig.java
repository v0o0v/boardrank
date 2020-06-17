//package net.boardrank;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.client.builder.AwsClientBuilder;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.annotation.Profile;
//
//@Slf4j
//@Configuration
//@Profile("test")
//@EnableDynamoDBRepositories(basePackages = {"net.boardrank.boardgame.domain.repository.dynamo"})
//public class AwsDynamoDbConfig {
//
//    @Bean
//    @Primary
//    public DynamoDBMapperConfig dynamoDBMapperConfig() {
//        return DynamoDBMapperConfig.DEFAULT;
//    }
//
//    @Bean(name = "amazonDynamoDB")
//    @Primary
//    public AmazonDynamoDB localAmazonDynamoDB() {
//        log.info("Start Local Amazon DynamoDB Client");
//        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials("test", "test");
//        return AmazonDynamoDBClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Regions.AP_NORTHEAST_2.getName()))
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
//        return new DynamoDBMapper(amazonDynamoDB, config);
//    }
//}
