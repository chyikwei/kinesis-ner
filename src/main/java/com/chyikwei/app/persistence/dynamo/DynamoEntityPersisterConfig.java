package com.chyikwei.app.persistence.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;

/**
 * Configuration of DynamoDB entity persister
 */
public class DynamoEntityPersisterConfig {

  private AmazonDynamoDB dynamoDB;
  private String tableName;
  private String hashKeyName;

  public DynamoEntityPersisterConfig(AmazonDynamoDB ddb, String table, String hashKey) {
    this.dynamoDB = ddb;
    this.tableName = table;
    this.hashKeyName = hashKey;
  }

  /**
   * Get DynamoDB table name
   *
   * @return table name
   */
  public String getTableName() {
    return tableName;
  }

  /**
   * Get DynamoDB hashKey name
   *
   * @return hashKey name
   */
  public String getHashKeyName() {
    return hashKeyName;
  }

  /**
   * Get AmazonDynamoDB
   *
   * @return AmazonDynamoDB instance
   */
  public AmazonDynamoDB getDynamoDB() {
    return dynamoDB;
  }

}
