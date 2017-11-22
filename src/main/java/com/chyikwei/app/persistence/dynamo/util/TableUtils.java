package com.chyikwei.app.persistence.dynamo.util;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import com.chyikwei.app.ner.Entity;
import com.chyikwei.app.ner.ObjectEntities;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersisterConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Utility for dynamoDB table
 */
public class TableUtils {

  public static String randomTableName() {
    return randomStringWithPrefix("Table_");
  }

  public static String randomKeyName() {
    return randomStringWithPrefix("HashKey_");
  }

  public static String randomStringWithPrefix(String prefix) {
    return prefix + UUID.randomUUID().toString().split("-", 2)[0];
  }


  private static final Log LOG = LogFactory.getLog(TableUtils.class);

  /**
   *
   * Get dynamoDB table is it's already existed. Otherwise create the table
   * hashKay and rangeKey. Note that the if the table schema can be different
   * if the table already existed
   *
   * @param ddb dynamoDB instance
   * @param config dynamoDB configuration
   * @return DynamoDB table
   */
  public static Table getOrcreateTable(DynamoDB ddb, DynamoEntityPersisterConfig config) {
    String tableName = config.getTableName();
    Table table;

    try {
      CreateTableRequest req = createRequest(config);
      table = ddb.createTable(req);
    } catch (ResourceInUseException e) {
      table = ddb.getTable(tableName);
    }
    return table;
  }

  /**
   * Create CreateTableRequest based on DynamoEntityPersisterConfig
   *
   * @param config
   * @return createRequest
   */
  public static CreateTableRequest createRequest(DynamoEntityPersisterConfig config) {

    String tableName = config.getTableName();
    String hashKey = config.getHashKeyName();
    String rangeKey = config.getRangeKeyName();

    ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
    keySchema.add(new KeySchemaElement().withAttributeName(hashKey).withKeyType(KeyType.HASH));

    ArrayList<AttributeDefinition> attr = new ArrayList<>();
    attr.add(new AttributeDefinition().withAttributeName(hashKey).withAttributeType("S"));
    ProvisionedThroughput throughput = new ProvisionedThroughput()
        .withReadCapacityUnits(config.getReadCapacity())
        .withWriteCapacityUnits(config.getWriteCapacity());

    if (rangeKey != null) {
      keySchema.add(new KeySchemaElement().withAttributeName(rangeKey).withKeyType(KeyType.RANGE));
      // use "S" for rage key type for now
      attr.add(new AttributeDefinition().withAttributeName(rangeKey).withAttributeType("S"));
    }

    CreateTableRequest request = new CreateTableRequest()
        .withTableName(tableName)
        .withKeySchema(keySchema)
        .withAttributeDefinitions(attr)
        .withProvisionedThroughput(throughput);

    return request;
  }

  /**
   *
   * Create DynamoDB Item from ObjectEntities
   *
   * @param obj entities inside an object
   * @param config dynamoDB config
   * @return dynamoDB item
   */
  public static Item objectEntitiesToDynamoItem(ObjectEntities obj, DynamoEntityPersisterConfig config) {

    Item item = new Item().withPrimaryKey(config.getHashKeyName(), obj.getUUID().toString());

    // iterate all fields and add to item
    for (Map.Entry<String, Set<Entity>> entry: obj.getEntities().entrySet()) {
      String field = entry.getKey();

      Set<String> set = new HashSet<>();
      for (Entity ent : entry.getValue()) {
        set.add(ent.toString());
      }
      item = item.withStringSet(field, set);
    }
    return item;
  }
}
