package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.kinesisfirehose.model.InvalidArgumentException;
import com.chyikwei.app.ner.ObjectEntitiesInterface;
import com.chyikwei.app.persistence.EntityPersisterInterface;

import java.util.List;

/**
 * DynamoDB implementation of EntityPersister
 */
public class DynamoEntityPersister implements EntityPersisterInterface {

  private DynamoDB dynamoDB;
  private DynamoEntityPersisterConfig config;

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
    //TODO: fix this
    //createTableIfNotExisted();
  }

  /**
   * Create DynamoDB table if not existed.
   *
   * If table already existed, will check hashKey and rangeKey
   *
   */
  private void createTableIfNotExisted() {

    String tableName = config.getTableName();
    String hashKeyName = config.getHashKeyName();

    assert dynamoDB != null;

    Table table = dynamoDB.getTable(tableName);

    if (table != null) {
      List<KeySchemaElement> schemas = table.getDescription().getKeySchema();

      if (schemas != null  && schemas.size() > 1) {
        //TODO: use right expcetion
        throw new InvalidArgumentException("table alreday exists with differnt KeySchema size");
      }
      if (schemas != null && !hashKeyName.equals(schemas.get(0).getAttributeName())) {
        throw new InvalidArgumentException("table alreday exists with differnt hashKey");
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persister(List<ObjectEntitiesInterface> objectEntities) {

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkpoint() {

  }
}
