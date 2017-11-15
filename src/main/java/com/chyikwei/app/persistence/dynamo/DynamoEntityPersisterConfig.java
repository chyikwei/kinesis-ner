package com.chyikwei.app.persistence.dynamo;


/**
 * Configuration of DynamoDB entity persister
 */
public class DynamoEntityPersisterConfig {

  private String tableName;
  private String hashKeyName;

  public DynamoEntityPersisterConfig(String table, String hashKey) {
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
}
