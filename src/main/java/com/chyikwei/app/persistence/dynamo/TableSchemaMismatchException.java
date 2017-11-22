package com.chyikwei.app.persistence.dynamo;


/**
 * Exception that DynamoDB table has mismatched schema
 */
public class TableSchemaMismatchException extends RuntimeException {

  public TableSchemaMismatchException(String message) {
    super(message);
  }

}
