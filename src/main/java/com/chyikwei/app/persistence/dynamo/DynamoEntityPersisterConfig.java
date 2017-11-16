package com.chyikwei.app.persistence.dynamo;


/**
 * Configuration of DynamoDB entity persister
 */
public class DynamoEntityPersisterConfig {

  public static final long DEFAULT_READ_CAPACITY = 10;
  public static final long DEFAULT_WRITE_CAPACITY = 10;

  private String tableName;
  private String hashKeyName;
  private String rangeKeyName;
  private long readCapacity;
  private long writeCapacity;

  public DynamoEntityPersisterConfig(String table, String hashKey) {

    this.tableName = table;
    this.hashKeyName = hashKey;

    // default range key is null
    this.rangeKeyName = null;
    this.readCapacity = DEFAULT_READ_CAPACITY;
    this.writeCapacity = DEFAULT_WRITE_CAPACITY;
  }

  /**
   * set range Key
   */
  public DynamoEntityPersisterConfig setRangeKeyName(String rangeKey) {
    this.rangeKeyName = rangeKey;
    return this;
  }

  /**
   * set read capacity
   */
  public DynamoEntityPersisterConfig setReadCapacity(long capacity) {
    this.readCapacity = capacity;
    return this;
  }
  /**
   * set write capacity
   */
  public DynamoEntityPersisterConfig setWriteCapacity(long capacity) {
    this.writeCapacity = capacity;
    return this;
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
   * Get DynamoDB read capacity
   *
   * @return hashKey name
   */
  public long getReadCapacity() {
    return readCapacity;
  }

  /**
   * Get DynamoDB write capacity
   *
   * @return hashKey name
   */
  public long getWriteCapacity() {
    return writeCapacity;
  }

  /**
   * Get DynamoDB rangeKey name
   *
   * @return hashKey name
   */
  public String getRangeKeyName() {
    return rangeKeyName;
  }
}
