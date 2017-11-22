package com.chyikwei.app.persistence.dynamo.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersister;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersisterConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TableUtilsTest {

  AmazonDynamoDB client;
  DynamoDB dynamoDB;
  DynamoEntityPersisterConfig config;
  DynamoEntityPersister persister;

  String test_table_name;
  String test_key_name;

  @Before
  public void setUp() throws Exception {
    test_table_name = TableUtils.randomTableName();
    test_key_name = TableUtils.randomKeyName();

    AmazonDynamoDB client = DynamoDBEmbedded.create().amazonDynamoDB();
    dynamoDB = new DynamoDB(client);
    config = new DynamoEntityPersisterConfig(test_table_name, test_key_name);
    persister = new DynamoEntityPersister(dynamoDB, config);
  }

  @After
  public void tearDown() throws Exception {
    Table table = dynamoDB.getTable(test_table_name);
    table.delete();
    table.waitForDelete();
  }

  @Test
  public void getOrcreateTable() throws Exception {
    DynamoEntityPersisterConfig config = new DynamoEntityPersisterConfig(test_table_name, test_key_name)
        .setReadCapacity(1000L)
        .setWriteCapacity(500L);
    Table t = TableUtils.getOrcreateTable(dynamoDB, config);
    TableDescription desc = t.describe();
    assertEquals(test_table_name, desc.getTableName());
    assertEquals(test_key_name, desc.getKeySchema().get(0).getAttributeName());
    assertEquals(1000L, (long)desc.getProvisionedThroughput().getReadCapacityUnits());
    assertEquals(500L, (long)desc.getProvisionedThroughput().getWriteCapacityUnits());
  }

}