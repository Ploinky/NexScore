package de.ploinky.NexScoreApp;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.ploinky.NexScoreApp.model.Player;
import de.ploinky.NexScoreApp.service.PlayerService;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = PlayerTest.DynamoDBInitializer.class)
public class PlayerTest {

    private static final int DYNAMODB_PORT = 8000;

    @Container
    public static GenericContainer dynamodb = new GenericContainer<>("amazon/dynamodb-local")
            .withExposedPorts(DYNAMODB_PORT);


    public static class DynamoDBInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext ctx) {

            TestPropertyValues.of(
                            String.format("amazon.dynamodb.endpoint: http://%s:%s",
                                    dynamodb.getContainerIpAddress(), dynamodb.getMappedPort(DYNAMODB_PORT)))
                    .applyTo(ctx);
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    @BeforeEach
    public void before() {
        amazonDynamoDB.createTable(
                new CreateTableRequest(Arrays.asList(new AttributeDefinition("name", "S")),
                        "Player",
                        Arrays.asList(new KeySchemaElement("name", KeyType.HASH)),
                        new ProvisionedThroughput(1L, 1L))
        );
    }

    @Test
    public void testPlayerPost() throws Exception {
        Player response = new Player("Hello, World!");
        final String expectedResponseContent = objectMapper.writeValueAsString(response);

        playerService.createPlayer(response);
    }
}