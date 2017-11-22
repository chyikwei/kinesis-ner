package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.chyikwei.app.model.MultiFieldEntities;
import com.chyikwei.app.persistence.EntityPersister;
import com.chyikwei.app.persistence.dynamo.util.TableUtils;

import java.util.List;

import static com.chyikwei.app.persistence.dynamo.util.TableUtils.getOrcreateTable;

/**
 * DynamoDB implementation of EntityPersister
 */
public class DynamoEntityPersister implements EntityPersister {

  private DynamoDB dynamoDB;
  private DynamoEntityPersisterConfig config;
  private Table dynamoTable;

  /**
   * create class
   */
  public DynamoEntityPersister(DynamoDB ddb, DynamoEntityPersisterConfig config) {
    this.dynamoDB = ddb;
    this.config = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void initialize() {
    // check and create table
    createTableIfNotExisted();
  }

  /**
   * Create DynamoDB table if not existed.
   *
   * If table already existed, will check hashKey and rangeKey
   *
   */
  private void createTableIfNotExisted() {

    dynamoTable = getOrcreateTable(dynamoDB, config);

    // check schema
    List<KeySchemaElement> schemas = dynamoTable.describe().getKeySchema();

    if (schemas != null && schemas.size() > 1) {
      throw new TableSchemaMismatchException("table already exists with different KeySchema size");
    }
    if (schemas != null && !config.getHashKeyName().equals(schemas.get(0).getAttributeName())) {
      throw new TableSchemaMismatchException("table already exists with different hashKey");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persister(List<MultiFieldEntities> multiFieldEntities) {

    for (MultiFieldEntities obj: multiFieldEntities) {
      persistSingleObject(obj);
    }
  }

  private void persistSingleObject(MultiFieldEntities obj) {
    //TODO: change to batch write later
    Item item = TableUtils.objectEntitiesToDynamoItem(obj, config);
    dynamoTable.putItem(item);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkpoint() {

  }
}
