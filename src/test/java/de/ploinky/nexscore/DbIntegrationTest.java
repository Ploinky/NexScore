package de.ploinky.nexscore;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
@ContextConfiguration(initializers = DbIntegrationTest.DynamoDbInitializer.class)
public class DbIntegrationTest {
    private static final int DYNAMODB_PORT = 8000;

    @Container
    public static GenericContainer dynamodb = new GenericContainer<>("amazon/dynamodb-local")
            .withExposedPorts(DYNAMODB_PORT);


    public static class DynamoDbInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ctx) {
            TestPropertyValues.of(
                String.format("amazon.dynamodb.endpoint: http://%s:%s",
                    dynamodb.getContainerIpAddress(), dynamodb.getMappedPort(DYNAMODB_PORT)))
                .applyTo(ctx);
        }
    }

    @Autowired
    protected AmazonDynamoDB amazonDynamoDb;

    @BeforeAll
    public static void before(@Autowired AmazonDynamoDB amazonDynamoDb) {
        amazonDynamoDb.createTable(
                new CreateTableRequest(Arrays.asList(new AttributeDefinition("puuid", "S")),
                        "Player",
                        Arrays.asList(new KeySchemaElement("puuid", KeyType.HASH)),
                        new ProvisionedThroughput(1L, 1L))
        );
    }
}
