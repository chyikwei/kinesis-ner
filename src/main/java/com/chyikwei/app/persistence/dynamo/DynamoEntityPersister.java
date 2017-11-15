package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.chyikwei.app.ner.ObjectEntitiesInterface;
import com.chyikwei.app.persistence.EntityPersisterInterface;

import java.util.List;

/**
 * DynamoDB implementation of EntityPersister
 */
public class DynamoEntityPersister implements EntityPersisterInterface {

  private DynamoEntityPersisterConfig config;
  /**
   * create class
   */
  public DynamoEntityPersister(DynamoEntityPersisterConfig config) {
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
    String tableName = config.getTableName();
    String hashKeyName = config.getHashKeyName();


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
