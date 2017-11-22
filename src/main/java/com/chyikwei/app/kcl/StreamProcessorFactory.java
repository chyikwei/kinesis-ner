package com.chyikwei.app.kcl;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.chyikwei.app.model.BaseMultiFieldEntitiesFactory;
import com.chyikwei.app.model.NewsTextRecordFactory;
import com.chyikwei.app.model.MultiFieldEntitiesFactory;
import com.chyikwei.app.model.TextRecordFactory;
import com.chyikwei.app.ner.*;
import com.chyikwei.app.persistence.EntityPersister;
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
    EntityExtractor entityExtractor = StanfordEntityExtractor.getInstance();
    EntityPersister persister = new DynamoEntityPersister(dynamoDB, dynamoConfig);
    TextRecordFactory textFactory = NewsTextRecordFactory.getInstance();
    MultiFieldEntitiesFactory objFactory = BaseMultiFieldEntitiesFactory.getInstance();

    return new StreamProcessor(entityExtractor, persister, textFactory, objFactory);
  }
}
