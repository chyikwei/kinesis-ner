package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;
import com.chyikwei.app.ner.Entity;
import com.chyikwei.app.ner.NewsObjectEntities;
import com.chyikwei.app.ner.ObjectEntities;
import com.chyikwei.app.persistence.dynamo.util.TableUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.*;

import static org.junit.Assert.*;

public class DynamoEntityPersisterTest {

  AmazonDynamoDB client;
  DynamoDB dynamoDB;
  DynamoEntityPersisterConfig config;
  DynamoEntityPersister persister;

  String test_table_name;
  String test_key_name;
  Random random;

  @Before
  public void setUp() throws Exception {
    random = new java.util.Random();

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
  public void initializeWithTableExisted() throws Exception {
    Table existed = createTable(test_table_name, test_key_name);
    persister.initialize();
  }


  @Test(expected = TableSchemaMismatchException.class)
  public void initializeWithInvalidTableExisted() throws Exception {
    createTable(test_table_name, "INVALID_KEY");
    persister.initialize();
  }

  @Test
  public void persister() throws Exception {

    List<ObjectEntities> objList = createNewsEntitiesList(100);
    persister.initialize();
    persister.persister(objList);

    // verify DDB result
    Table table = dynamoDB.getTable(config.getTableName());
    for (ObjectEntities obj : objList) {
      checkTableContains(table, obj);
    }
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


  private List<ObjectEntities> createNewsEntitiesList(int size) {
    List<ObjectEntities> newsList = new LinkedList<>();
    for (int i=0; i < size; i++) {
      newsList.add(createNewsEntities());
    }
    return newsList;
  }

  private ObjectEntities createNewsEntities() {
    ObjectEntities news = new NewsObjectEntities(UUID.randomUUID());
    for (int i=0; i < 10; i++) {
      String field = "Field_" + i;
      for (int j=0; j < 10; j++) {
        String type;
        if (random.nextInt(2) % 2 == 0) {
          type = "PERSON";
        } else {
          type = "ORGANIZATION";
        }
        news.addEntities(field, new Entity(type, "entity_" + j));
      }
    }
    return news;
  }

  private void checkTableContains(Table table, ObjectEntities obj) {

    String key = obj.getUUID().toString();
    Item item = table.getItem(config.getHashKeyName(), key);
    assertNotNull(item);

    for (Map.Entry<String, Set<Entity>> entry: obj.getEntities().entrySet()) {
      String field = entry.getKey();

      // check field in item
      assertTrue(item.hasAttribute(field));

      Set<String> s = item.getStringSet(field);
      for (Entity entity : entry.getValue()) {
        // check entity in field
        assertTrue(s.contains(entity.toString()));
      }
    }
  }
}