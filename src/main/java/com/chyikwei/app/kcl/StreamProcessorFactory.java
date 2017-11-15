package com.chyikwei.app.kcl;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.chyikwei.app.ner.EntityExtractInterface;
import com.chyikwei.app.ner.StanfordEntityExtractor;
import com.chyikwei.app.persistence.EntityPersisterInterface;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersister;
import com.chyikwei.app.persistence.dynamo.DynamoEntityPersisterConfig;


/**
 * Used to create StreamProcessor
 */
public class StreamProcessorFactory implements IRecordProcessorFactory {

  private DynamoEntityPersisterConfig dynamoConfig;
  private DynamoDB dynamoDB;
  public StreamProcessorFactory(DynamoDB ddb, DynamoEntityPersisterConfig config) {
    dynamoDB = ddb;
    dynamoConfig = config;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRecordProcessor createProcessor() {
    EntityExtractInterface entityExtractor = StanfordEntityExtractor.getInstance();
    EntityPersisterInterface persister = new DynamoEntityPersister(dynamoDB, dynamoConfig);
    return new StreamProcessor(entityExtractor, persister);
  }
}
