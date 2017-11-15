package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class DynamoEntityPersisterTest {

  AmazonDynamoDB client;
  DynamoDB dynamoDB;
  DynamoEntityPersisterConfig config;
  DynamoEntityPersister persister;

  final String TEST_TABLE_NAME = "DDB_TEST_TABLE";
  final String TEST_TABLE_KEY = "DDB_TEST_KEY";

  @Before
  public void setUp() throws Exception {
    AmazonDynamoDB client = DynamoDBEmbedded.create().amazonDynamoDB();
    dynamoDB = new DynamoDB(client);
    config = new DynamoEntityPersisterConfig(TEST_TABLE_NAME, TEST_TABLE_KEY);
    persister = new DynamoEntityPersister(dynamoDB, config);
  }

  @After
  public void tearDown() throws Exception {
    //client.shutdown();
  }

  @Test
  public void initializeWithTableExisted() throws Exception {
    Table existed = createTable(TEST_TABLE_NAME, TEST_TABLE_KEY);
    assertTrue(persister != null);
    persister.initialize();
  }

  private Table createTable(String tableName, String hasKey) {

    ArrayList<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
    keySchema.add(new KeySchemaElement().withAttributeName(hasKey).withKeyType(KeyType.HASH));

    ArrayList<AttributeDefinition> attr = new ArrayList<AttributeDefinition>();
    attr.add(new AttributeDefinition().withAttributeName(hasKey).withAttributeType("S"));
    ProvisionedThroughput throughput = new ProvisionedThroughput()
        .withReadCapacityUnits(1000L)
        .withWriteCapacityUnits(1000L);

    CreateTableRequest request = new CreateTableRequest()
        .withTableName(tableName)
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attr)
        .withProvisionedThroughput(throughput);

    Table table = dynamoDB.createTable(request);
    return table;
  }
}