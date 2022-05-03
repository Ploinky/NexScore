package de.ploinky.nexscore;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDynamoDBRepositories(basePackages = "de.ploinky.nexscore.repository")
public class DynamoDbConfig {
    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDbEndpoint;
    
    @Value("${amazon.dynamodb.region}")
    private String amazonDynamoDbRegion;

    @Value("${amazon.aws.accesskey}")
    private String amazonAwsAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAwsSecretKey;
    //CHECKSTYLE:OFF
    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        //CHECKSTYLE:ON
        AmazonDynamoDBClientBuilder builder =
            AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration(
                            amazonDynamoDbEndpoint, amazonDynamoDbRegion))
            .withCredentials(amazonAwsCredentials());

        AmazonDynamoDB amazonDynamoDb = builder.build();

        return amazonDynamoDb;
    }

    @Bean
    public AWSCredentialsProvider amazonAwsCredentials() {
        return new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                return new BasicAWSCredentials(amazonAwsAccessKey, amazonAwsSecretKey);
            }

            @Override
            public void refresh() {

            }
        };
    }
}
